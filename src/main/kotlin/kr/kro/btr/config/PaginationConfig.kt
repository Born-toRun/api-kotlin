package kr.kro.btr.config

import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode

@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
class PaginationConfig 
