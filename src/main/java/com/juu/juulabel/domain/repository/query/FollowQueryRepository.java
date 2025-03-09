package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.follow.FollowUser;
import com.juu.juulabel.domain.entity.alcohol.AlcoholType;
import com.juu.juulabel.domain.entity.alcohol.QAlcoholType;
import com.juu.juulabel.domain.entity.follow.QFollow;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.member.QMember;
import com.juu.juulabel.domain.entity.member.QMemberAlcoholType;
import com.juu.juulabel.domain.repository.writer.FollowWriter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QFollow follow = QFollow.follow;
    QMember follower = new QMember("follower");
    QMember followee = new QMember("followee");
    QMember members = new QMember("member");
    QMemberAlcoholType memberAlcoholType = QMemberAlcoholType.memberAlcoholType;
    QAlcoholType alcoholType = QAlcoholType.alcoholType;

    public Slice<FollowUser> findAllFollowing(Member loginMember, Member member, Long lastFollowId, int pageSize) {
        List<FollowUser> followingList = jpaQueryFactory
                .select(
                        Projections.constructor(
                                FollowUser.class,
                                followee.id,
                                followee.nickname,
                                followee.profileImage,
                                jpaQueryFactory
                                        .selectFrom(follow)
                                        .where(
                                                follow.follower.eq(loginMember),
                                                follow.followee.eq(followee)
                                        )
                                        .exists()
                        ))
                .from(follow)
                .innerJoin(follow.follower, follower)
                .innerJoin(follow.followee, followee)
                .where(
                        follower.eq(member),
                        noOffsetByFollowId(follow, lastFollowId)
                )
                .orderBy(followIdDesc(follow))
                .limit(pageSize + 1L)
                .fetch();

        boolean hasNext = followingList.size() > pageSize;
        if (hasNext) {
            followingList.remove(pageSize);
        }

        return new SliceImpl<>(followingList, PageRequest.ofSize(pageSize), hasNext);
    }

    public Slice<FollowUser> findAllFollower(Member loginMember, Member member, Long lastFollowId, int pageSize) {
        List<FollowUser> followerList = jpaQueryFactory
                .select(
                        Projections.constructor(
                                FollowUser.class,
                                follower.id,
                                follower.nickname,
                                follower.profileImage,
                                jpaQueryFactory
                                        .selectFrom(follow)
                                        .where(
                                                follow.follower.eq(loginMember),
                                                follow.followee.eq(follower)
                                        )
                                        .exists()
                        ))
                .from(follow)
                .innerJoin(follow.follower, follower)
                .innerJoin(follow.followee, followee)
                .where(
                        followee.eq(member),
                        noOffsetByFollowId(follow, lastFollowId)
                )
                .orderBy(followIdDesc(follow))
                .limit(pageSize + 1L)
                .fetch();

        boolean hasNext = followerList.size() > pageSize;
        if (hasNext) {
            followerList.remove(pageSize);
        }

        return new SliceImpl<>(followerList, PageRequest.ofSize(pageSize), hasNext);
    }

    public long countFollowing(final Member member){
        return jpaQueryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.follower.eq(member))
                .fetchOne();
    }

    public long countFollower(final Member member){
        return jpaQueryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.followee.eq(member))
                .fetchOne();
    }

    public Slice<FollowUser> getSearchUserList(Member loginMember, Long lastFollowId, int pageSize, String username){
        List<FollowUser> searchUserList = jpaQueryFactory
                .select(
                        Projections.constructor(
                                FollowUser.class,
                                members.id,
                                members.nickname,
                                members.profileImage,
                                jpaQueryFactory
                                        .selectFrom(follow)
                                        .where(
                                                follow.follower.eq(loginMember),
                                                follow.followee.eq(members)
                                        )
                                        .exists()
                        ))
                .from(members)
                .where(
                        members.nickname.contains(username)
                )
                .limit(pageSize + 1L)
                .fetch();

        boolean hasNext = searchUserList.size() > pageSize;
        if (hasNext) {
            searchUserList.remove(pageSize);
        }
        return new SliceImpl<>(searchUserList, PageRequest.ofSize(pageSize), hasNext);
    }

    public boolean isFollowing(final Member loginMember, final Member member){

        Long result = jpaQueryFactory
                .select(follow.count())
                .from(follow)
                .where(
                        follow.follower.eq(loginMember),
                        follow.followee.eq(member)
                        )
                .fetchOne();

        return result != null && result > 0;
    }

    public Slice<FollowUser> findBadgeRecommendUserList(final Member loginMember, Long lastFollowId, int pageSize){
        List<FollowUser> BadgeRecommendUserList = jpaQueryFactory
                .select(
                        Projections.constructor(
                                FollowUser.class,
                                members.id,
                                members.nickname,
                                members.profileImage,
                                jpaQueryFactory
                                        .selectFrom(follow)
                                        .where(
                                                follow.follower.eq(loginMember),
                                                follow.followee.eq(members)
                                        )
                                        .exists()
                        )
                )
                .from(members)
                .where(
                        members.hasBadge.isTrue(),
                        noOffsetByFollowId(members, lastFollowId)
                )
                .limit(pageSize + 1L)
                .fetch();

        boolean hasNext = BadgeRecommendUserList.size() > pageSize;
        if (hasNext) {
            BadgeRecommendUserList.remove(pageSize);
        }

        return new SliceImpl<>(BadgeRecommendUserList, PageRequest.ofSize(pageSize), hasNext);
    }

    public Slice<FollowUser> findTastingRecommendUserList(final Member loginMember, Long lastFollowId, int pageSize){

        List<AlcoholType> preferredAlcoholTypes = jpaQueryFactory
                .select(alcoholType)
                .from(memberAlcoholType)
                .join(memberAlcoholType.alcoholType, alcoholType)
                .where(memberAlcoholType.member.eq(loginMember))
                .fetch();

        System.out.println("preferredAlcoholTypes = " + preferredAlcoholTypes.stream()
                .map(AlcoholType::getId)
                .toList());


        List<FollowUser> TastingRecommendUserList = jpaQueryFactory
                .select(
                        Projections.constructor(
                                FollowUser.class,
                                members.id,
                                members.nickname,
                                members.profileImage,
                                jpaQueryFactory
                                        .selectFrom(follow)
                                        .where(
                                                follow.follower.eq(loginMember),
                                                follow.followee.eq(members)
                                        )
                                        .exists()
                        )
                )
                .from(members)
                .join(memberAlcoholType).on(members.id.eq(memberAlcoholType.member.id))
                .join(memberAlcoholType.alcoholType, alcoholType)
                .where(
                        memberAlcoholType.alcoholType.in(preferredAlcoholTypes),
                        members.ne(loginMember),
                        noOffsetByFollowId(members, lastFollowId)
                )
                .limit(pageSize + 1L)
                .fetch();

        boolean hasNext = TastingRecommendUserList.size() > pageSize;
        if (hasNext) {
            TastingRecommendUserList.remove(pageSize);
        }

        return new SliceImpl<>(TastingRecommendUserList, PageRequest.ofSize(pageSize), hasNext);
    }


    private OrderSpecifier<Long> followIdDesc(QFollow follow) {
        return follow.id.desc();
    }

    private BooleanExpression noOffsetByFollowId(QFollow follow, Long lastFollowId) {
        return Objects.isEmpty(lastFollowId) ? null : follow.id.lt(lastFollowId);
    }

    private BooleanExpression noOffsetByFollowId(QMember members, Long lastFollowId) {
        return Objects.isEmpty(lastFollowId) ? null : members.id.lt(lastFollowId);
    }
}

