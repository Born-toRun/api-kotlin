package kr.kro.btr.adapter.out.persistence.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.kro.btr.domain.entity.ActivityEntity
import kr.kro.btr.domain.entity.QActivityEntity
import kr.kro.btr.infrastructure.model.SearchAllActivityQuery
import org.springframework.stereotype.Component

@Component
class ActivityQuery(
    private val queryFactory: JPAQueryFactory
) {
    fun searchAllByFilter(query: SearchAllActivityQuery): List<ActivityEntity> {
        val activity = QActivityEntity.activityEntity

        var whereClause: BooleanExpression = activity.userEntity.crewId.eq(query.myCrewId)
        var optionalWhereClause: BooleanExpression? = null

        if (query.courses?.isNotEmpty() == false) {
            for (course in query.courses) {
                optionalWhereClause = optionalWhereClause?.or(activity.course.contains(course))
                    ?: activity.course.contains(course)
            }

            whereClause = whereClause.and(optionalWhereClause)
        }

        return queryFactory
            .selectFrom(activity)
            .where(whereClause)
            .orderBy(activity.id.asc())
            .fetch()
    }
}
