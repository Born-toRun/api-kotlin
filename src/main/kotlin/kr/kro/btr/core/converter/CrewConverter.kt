package kr.kro.btr.core.converter

import kr.kro.btr.adapter.`in`.web.payload.CreateCrewRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailCrewResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCrewResponse
import kr.kro.btr.domain.entity.CrewEntity
import kr.kro.btr.domain.port.model.CreateCrewCommand
import kr.kro.btr.domain.port.model.Crew
import kr.kro.btr.infrastructure.model.CreateCrewQuery
import org.springframework.stereotype.Component

@Component
class CrewConverter {

    fun map(source: List<Crew>): SearchCrewResponse {
        val crewDetails: List<SearchCrewResponse.CrewDetail> = source.map { crew ->
            SearchCrewResponse.CrewDetail(
                id = crew.id,
                crewName = crew.name,
                contents = crew.contents,
                region = crew.region,
                imageUri = crew.imageUri,
                logoUri = crew.logoUri,
                crewSnsUri = crew.sns
            )
        }

        return  SearchCrewResponse(crewDetails)
    }

    fun map(source: Crew): DetailCrewResponse {
        return DetailCrewResponse(
            id = source.id,
            crewName = source.name,
            contents = source.contents,
            region = source.region,
            imageUri = source.imageUri,
            logoUri = source.logoUri,
            crewSnsUri = source.sns
        )
    }

    fun map(source: CreateCrewRequest): CreateCrewCommand {
        return CreateCrewCommand(
            name = source.name,
            contents = source.contents,
            sns = source.sns,
            region = source.region,
        )
    }

    fun map(source: CrewEntity): Crew {
        return Crew(
            id = source.id,
            name = source.name,
            contents = source.contents,
            region = source.region,
            sns = source.sns,
            imageUri = source.imageEntity?.fileUri,
            logoUri = source.logoEntity?.fileUri
        )
    }

    fun map(source: List<CrewEntity>): List<Crew> {
        return source.map { crewEntity ->
            map(crewEntity)
        }
    }

    fun map(source: CreateCrewCommand): CreateCrewQuery {
        return CreateCrewQuery(
            name = source.name,
            contents = source.contents,
            sns = source.sns,
            region = source.region,
        )
    }

    fun map(source: CreateCrewQuery): CrewEntity {
        return CrewEntity(
            name = source.name,
            contents = source.contents,
            sns = source.sns,
            region = source.region,
        )
    }
}
