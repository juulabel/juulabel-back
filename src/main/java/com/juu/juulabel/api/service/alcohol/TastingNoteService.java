package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.request.SearchAlcoholDrinksListRequest;
import com.juu.juulabel.api.dto.request.TastingNoteListRequest;
import com.juu.juulabel.api.dto.request.TastingNoteWriteRequest;
import com.juu.juulabel.api.dto.response.*;
import com.juu.juulabel.api.factory.SliceResponseFactory;
import com.juu.juulabel.api.service.s3.S3Service;
import com.juu.juulabel.common.constants.FileConstants;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.ImageInfo;
import com.juu.juulabel.domain.dto.alcohol.*;
import com.juu.juulabel.domain.dto.s3.UploadImageInfo;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteDetailInfo;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSummary;
import com.juu.juulabel.domain.embedded.AlcoholicDrinksSnapshot;
import com.juu.juulabel.domain.entity.alcohol.*;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.tastingnote.*;
import com.juu.juulabel.domain.repository.TastingNoteLikeReader;
import com.juu.juulabel.domain.repository.reader.*;
import com.juu.juulabel.domain.repository.writer.TastingNoteImageWriter;
import com.juu.juulabel.domain.repository.writer.TastingNoteLikeWriter;
import com.juu.juulabel.domain.repository.writer.TastingNoteWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TastingNoteService {

    private final TastingNoteReader tastingNoteReader;
    private final AlcoholTypeReader alcoholTypeReader;
    private final AlcoholicDrinksReader alcoholicDrinksReader;
    private final AlcoholTypeColorReader alcoholTypeColorReader;
    private final AlcoholTypeScentReader alcoholTypeScentReader;
    private final AlcoholTypeFlavorReader alcoholTypeFlavorReader;
    private final AlcoholTypeSensoryReader alcoholTypeSensoryReader;
    private final S3Service s3Service;
    private final TastingNoteWriter tastingNoteWriter;
    private final TastingNoteImageWriter tastingNoteImageWriter;
    private final TastingNoteImageReader tastingNoteImageReader;
    private final TastingNoteLikeReader tastingNoteLikeReader;
    private final TastingNoteLikeWriter tastingNoteLikeWriter;

    @Transactional(readOnly = true)
    public AlcoholDrinksListResponse searchAlcoholDrinksList(final SearchAlcoholDrinksListRequest request) {
        final Slice<AlcoholicDrinksSummary> alcoholicDrinks = tastingNoteReader.getAllAlcoholicDrinks(request.search(), request.lastAlcoholicDrinksName(), request.pageSize());
        return SliceResponseFactory.create(
                AlcoholDrinksListResponse.class,
                alcoholicDrinks.isLast(),
                alcoholicDrinks.getContent()
        );
    }

    @Transactional(readOnly = true)
    public TastingNoteColorListResponse loadTastingNoteColorsList(final Long alcoholTypeId) {
        final List<ColorInfo> colors = alcoholTypeColorReader.getAllColorInfoByAlcoholTypeId(alcoholTypeId);
        return new TastingNoteColorListResponse(colors);
    }

    @Transactional(readOnly = true)
    public TastingNoteSensoryListResponse loadTastingNoteSensoryList(final Long alcoholTypeId) {
        final List<SensoryLevelInfo> sensoryLevels = alcoholTypeSensoryReader.getAllSensoryLevelInfoByAlcoholTypeId(alcoholTypeId);
        return new TastingNoteSensoryListResponse(sensoryLevels);
    }

    @Transactional(readOnly = true)
    public TastingNoteScentListResponse loadTastingNoteScentList(final Long alcoholTypeId) {
        final List<CategoryWithScentSummary> categories = alcoholTypeScentReader.getAllCategoryWithScentByAlcoholTypeId(alcoholTypeId);
        return new TastingNoteScentListResponse(categories);
    }

    @Transactional(readOnly = true)
    public TastingNoteFlavorListResponse loadTastingNoteFlavorList(final Long alcoholTypeId) {
        final List<FlavorLevelInfo> flavorLevels = alcoholTypeFlavorReader.getAllFlavorLevelInfoByAlcoholTypeId(alcoholTypeId);
        return new TastingNoteFlavorListResponse(flavorLevels);
    }

    @Transactional
    public TastingNoteWriteResponse write(final Member loginMember, final TastingNoteWriteRequest request, List<MultipartFile> files) {
        // 1. 입력된 주종 확인
        final Long alcoholTypeId = request.alcoholTypeId();
        final AlcoholType alcoholType = alcoholTypeReader.getById(alcoholTypeId);

        // 2. 전통주 정보 확인 (OD, UD)
        final AlcoholicDrinks alcoholicDrinks = alcoholicDrinksReader.getByIdOrElseNull(request.alcoholicDrinksId());
        final AlcoholicDrinksSnapshot alcoholicDrinksInfo = AlcoholicDrinksSnapshot.fromDto(request.alcoholicDrinksDetails());

        // 3. 감각 정보 확인 (시각 정보, 촉각 정보, 미각 정보, 후각 정보)
        final Color color = getValidColorOrElseThrow(alcoholTypeId, request.colorId());
        final List<Scent> scents = getValidScentsOrElseThrow(alcoholTypeId, request.scentIds());
        final List<FlavorLevel> flavorLevels = getValidFlavorLevelsOrElseThrow(alcoholTypeId, request.flavorLevelIds());
        final List<SensoryLevel> sensoryLevels = getValidSensoryLevelsOrElseThrow(alcoholTypeId, request.sensoryLevelIds());

        // 4. 시음 노트 정보 생성 (작성)
        final TastingNote tastingNote = createBy(loginMember, alcoholType, alcoholicDrinks, color, alcoholicDrinksInfo, request);
        final TastingNote result = tastingNoteWriter.create(
                tastingNote,
                TastingNoteScent.of(tastingNote, scents),
                TastingNoteFlavorLevel.of(tastingNote, flavorLevels),
                TastingNoteSensoryLevel.of(tastingNote, sensoryLevels));

        List<String> imageUrlList = new ArrayList<>();
        storeImageList(files, imageUrlList, tastingNote);

        return TastingNoteWriteResponse.fromEntity(result);
    }

    private Color getValidColorOrElseThrow(final Long alcoholTypeId, final Long colorId) {
        final List<Color> colors = alcoholTypeColorReader.getAllColorByAlcoholTypeId(alcoholTypeId);
        return colors.stream()
                .filter(c -> Objects.equals(c.getId(), colorId))
                .findFirst()
                .orElseThrow(() -> new InvalidParamException(ErrorCode.INVALID_ALCOHOL_TYPE_COLOR));
    }

    private List<Scent> getValidScentsOrElseThrow(final Long alcoholTypeId, final List<Long> scentIds) {
        final List<Scent> scents = alcoholTypeScentReader.getAllScentByAlcoholTypeId(alcoholTypeId);
        final Map<Long, Scent> scentMap = scents.stream()
                .collect(Collectors.toMap(Scent::getId, scent -> scent));
        return scentIds.stream()
                .map(scentId ->
                        Optional.ofNullable(scentMap.get(scentId))
                                .orElseThrow(() -> new InvalidParamException(ErrorCode.INVALID_ALCOHOL_TYPE_SCENT))
                )
                .toList();
    }

    private List<SensoryLevel> getValidSensoryLevelsOrElseThrow(final Long alcoholTypeId, final List<Long> sensoryLevelIds) {
        final List<SensoryLevel> sensoryLevels = alcoholTypeSensoryReader.getAllSensoryLevelByAlcoholTypeId(alcoholTypeId);
        final Map<Long, SensoryLevel> sensoryLevelMap = sensoryLevels.stream()
                .collect(Collectors.toMap(SensoryLevel::getId, sensoryLevel -> sensoryLevel));
        return sensoryLevelIds.stream()
                .map(sensoryLevelId ->
                        Optional.ofNullable(sensoryLevelMap.get(sensoryLevelId))
                                .orElseThrow(() -> new InvalidParamException(ErrorCode.INVALID_ALCOHOL_TYPE_SENSORY))
                )
                .toList();
    }

    private List<FlavorLevel> getValidFlavorLevelsOrElseThrow(final Long alcoholTypeId, final List<Long> flavorLevelIds) {
        final List<FlavorLevel> flavorLevels = alcoholTypeFlavorReader.getAllFlavorLevelByAlcoholTypeId(alcoholTypeId);
        final Map<Long, FlavorLevel> flavorMap = flavorLevels.stream()
                .collect(Collectors.toMap(FlavorLevel::getId, flavorLevel -> flavorLevel));
        return flavorLevelIds.stream()
                .map(flavorLevelId ->
                        Optional.ofNullable(flavorMap.get(flavorLevelId))
                                .orElseThrow(() -> new InvalidParamException(ErrorCode.INVALID_ALCOHOL_TYPE_FLAVOR))
                )
                .toList();
    }

    private TastingNote createBy(final Member member,
                                 final AlcoholType alcoholType,
                                 final AlcoholicDrinks alcoholicDrinks,
                                 final Color color,
                                 final AlcoholicDrinksSnapshot alcoholicDrinksInfo,
                                 final TastingNoteWriteRequest request) {
        return TastingNote.of(
                member,
                alcoholType,
                alcoholicDrinks,
                color,
                alcoholicDrinksInfo,
                request.rating(),
                request.content(),
                request.isPrivate()
        );
    }

    private void storeImageList(
        final List<MultipartFile> files,
        final List<String> newImageUrlList,
        final TastingNote tastingNote
    ) {
        if (!Objects.isNull(files) && !files.isEmpty()) {
            // TODO : 파일 크기 및 확장자 validate
            validateFileListSize(files);

            for (MultipartFile file : files) {
                UploadImageInfo uploadImageInfo = s3Service.uploadTastingNoteImage(file);
                newImageUrlList.add(uploadImageInfo.ImageUrl());
                tastingNoteImageWriter.store(tastingNote, newImageUrlList.size(), uploadImageInfo.ImageUrl());
            }
        }
    }

    private void validateFileListSize(final List<MultipartFile> nonEmptyFiles) {
        if (nonEmptyFiles.size() > FileConstants.FILE_MAX_SIZE_COUNT) {
            throw new InvalidParamException(ErrorCode.EXCEEDED_FILE_COUNT);
        }
    }


    @Transactional(readOnly = true)
    public TastingNoteListResponse loadTastingNoteList(Member member, TastingNoteListRequest request) {
        Slice<TastingNoteSummary> tastingNoteList =
            tastingNoteReader.getAllTastingNotes(member, request.lastTastingNoteId(), request.pageSize());

        return new TastingNoteListResponse(tastingNoteList);
    }

    @Transactional(readOnly = true)
    public TastingNoteResponse loadTastingNote(Member member, Long tastingNoteId) {
        TastingNoteDetailInfo tastingNoteDetailInfo = tastingNoteReader.getTastingNoteDetailById(tastingNoteId, member);
        List<String> urlList = tastingNoteImageReader.getImageUrlList(tastingNoteId);

        return new TastingNoteResponse(
            tastingNoteDetailInfo,
            tastingNoteReader.getSensoryLevelIds(tastingNoteId),
            tastingNoteReader.getScentIds(tastingNoteId),
            tastingNoteReader.getFlavorLevelIds(tastingNoteId),
            new ImageInfo(urlList, urlList.size())
        );
    }

    @Transactional
    public TastingNoteWriteResponse updateTastingNote(
        Member member,
        Long tastingNoteId,
        TastingNoteWriteRequest request,
        List<MultipartFile> files
    ) {
        TastingNote tastingNote = getTastingNote(tastingNoteId);
        validateTastingNoteWriter(member, tastingNote);

        // 입력된 주종 확인
        final Long alcoholTypeId = request.alcoholTypeId();
        final AlcoholType alcoholType = alcoholTypeReader.getById(alcoholTypeId);

        // 전통주 정보 확인 (OD, UD)
        final AlcoholicDrinks alcoholicDrinks = alcoholicDrinksReader.getByIdOrElseNull(request.alcoholicDrinksId());
        final AlcoholicDrinksSnapshot alcoholicDrinksInfo = AlcoholicDrinksSnapshot.fromDto(request.alcoholicDrinksDetails());

        // 감각 정보 확인 (시각 정보, 촉각 정보, 미각 정보, 후각 정보)
        final Color color = getValidColorOrElseThrow(alcoholTypeId, request.colorId());
        final List<Scent> scents = getValidScentsOrElseThrow(alcoholTypeId, request.scentIds());
        final List<FlavorLevel> flavorLevels = getValidFlavorLevelsOrElseThrow(alcoholTypeId, request.flavorLevelIds());
        final List<SensoryLevel> sensoryLevels = getValidSensoryLevelsOrElseThrow(alcoholTypeId, request.sensoryLevelIds());

        tastingNote.update(alcoholType, alcoholicDrinks, color, alcoholicDrinksInfo, request.rating(), request.content(), request.isPrivate());
        tastingNoteWriter.update(
            tastingNote.getId(),
            TastingNoteScent.of(tastingNote, scents),
            TastingNoteFlavorLevel.of(tastingNote, flavorLevels),
            TastingNoteSensoryLevel.of(tastingNote, sensoryLevels));


        List<TastingNoteImage> tastingNoteImageList = tastingNoteImageReader.getImageList(tastingNote.getId());
        tastingNoteImageList.forEach(TastingNoteImage::delete);

        List<String> imageUrlList = new ArrayList<>();
        storeImageList(files, imageUrlList, tastingNote);

        return new TastingNoteWriteResponse(tastingNote.getId());
    }

    private TastingNote getTastingNote(Long tastingNoteId) {
        return tastingNoteReader.getById(tastingNoteId);
    }

    private static void validateTastingNoteWriter(Member member, TastingNote tastingNote) {
        if (!member.getId().equals(tastingNote.getMember().getId())) {
            throw new InvalidParamException(ErrorCode.NOT_TASTING_NOTE_WRITER);
        }
    }

    @Transactional
    public DeleteTastingNoteResponse deleteTastingNote(Member member, Long tastingNoteId) {
        TastingNote tastingNote = getTastingNote(tastingNoteId);
        validateTastingNoteWriter(member, tastingNote);

        tastingNote.delete();
        return new DeleteTastingNoteResponse(tastingNote.getId());
    }

    @Transactional
    public boolean toggleTastingNoteLike(Member member, Long tastingNoteId) {
        TastingNote tastingNote = getTastingNote(tastingNoteId);
        Optional<TastingNoteLike> tastingNoteLike = tastingNoteLikeReader.findByMemberAndTastingNote(member, tastingNote);

        // 좋아요가 등록되어 있다면 삭제, 등록되어 있지 않다면 등록
        return tastingNoteLike
            .map(like -> {
                tastingNoteLikeWriter.delete(like);
                return false;
            })
            .orElseGet(() -> {
                tastingNoteLikeWriter.store(member, tastingNote);
                return true;
            });
    }
}
