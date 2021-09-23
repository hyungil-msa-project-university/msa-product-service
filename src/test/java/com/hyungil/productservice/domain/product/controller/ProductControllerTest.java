package com.hyungil.productservice.domain.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
import com.hyungil.productservice.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductControllerTest {

	private final ObjectMapper objectMapper;
	private final WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Autowired
	ProductControllerTest(ObjectMapper objectMapper, WebApplicationContext webApplicationContext) {
		this.objectMapper = objectMapper;
		this.webApplicationContext = webApplicationContext;
	}

	@MockBean
	private ProductService productService;

	AddProductRequestDto addProductRequestDto = AddProductRequestDto.builder()
		.productName("상품")
		.build();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.alwaysDo(print())
			.build();
	}

	@Test
	@DisplayName("상품 등록에 성공할 경우 Http Status Code 201(Created)를 리턴")
	void addProduct() throws Exception {

		doNothing().when(productService).addProduct(addProductRequestDto);

		mockMvc.perform(post("/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(addProductRequestDto))
		)
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("상품 이름에 Null을 등록하여 실패한 경우 Http Status Code 400(BadRequest)를 리턴")
	void addProductIfNameIsNull() throws Exception {

		addProductRequestDto = AddProductRequestDto.builder()
			.productName("")
			.build();

		mockMvc.perform(post("/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(addProductRequestDto))
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("상품 이름은 필수 입력입니다."));
	}

	@Test
	@DisplayName("상품 이름에 null이나 빈 값 또는 공 백을 등록하여 실패한 경우 Http Status Code 400(BadRequest)를 리턴")
	void addProductIfNameEmptyString() throws Exception {

		addProductRequestDto = AddProductRequestDto.builder()
			.productName(" ")
			.build();

		mockMvc.perform(post("/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(addProductRequestDto))
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("상품 이름은 필수 입력입니다."));
	}

	@Test
	@DisplayName("상품 이름에 null이나 공백을 등록하여 실패한 경우 Http Status Code 400(BadRequest)를 리턴")
	void addProductIfNameIsBlankString() throws Exception {

		addProductRequestDto = AddProductRequestDto.builder()
			.productName(null)
			.build();

		mockMvc.perform(post("/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(addProductRequestDto))
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("상품 이름은 필수 입력입니다."));
	}

	private String toJsonString(Object dto) throws JsonProcessingException {
		return objectMapper.writeValueAsString(dto);
	}
}