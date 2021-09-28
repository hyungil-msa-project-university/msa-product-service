package com.hyungil.productservice.domain.product.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
import com.hyungil.productservice.domain.product.dto.request.UpdateProductRequestDto;
import com.hyungil.productservice.domain.product.dto.response.GetProductResponseDto;
import com.hyungil.productservice.domain.product.repository.ProductRepository;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductApi.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductApiTest {

	private final ObjectMapper objectMapper;
	private final WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Autowired
	ProductApiTest(ObjectMapper objectMapper, WebApplicationContext webApplicationContext) {
		this.objectMapper = objectMapper;
		this.webApplicationContext = webApplicationContext;
	}

	@MockBean
	private ProductService productService;

	@MockBean
	private ProductRepository productRepository;

	AddProductRequestDto addProductRequestDto = AddProductRequestDto.builder()
		.productName("상품")
		.build();

	GetProductResponseDto getProductResponseDto = GetProductResponseDto.builder()
		.productName("상품")
		.build();

	UpdateProductRequestDto updateProductRequestDto = UpdateProductRequestDto.builder()
		.productName("상품상품")
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
			.andExpect(jsonPath("$.message").value("잘못된 상품 이름입니다."));
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
			.andExpect(jsonPath("$.message").value("잘못된 상품 이름입니다."));
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
			.andExpect(jsonPath("$.message").value("잘못된 상품 이름입니다."));
	}

	@Test
	@DisplayName("특정 상품 조회에 성공할 경우 Http Status Code 201(Created)를 리턴")
	void getProduct() throws Exception {

		given(productService.getProduct(1L)).willReturn(getProductResponseDto);

		mockMvc.perform(get("/products/1")
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productName").value("상품"));
	}

	@Test
	@DisplayName("전체 상품 조회 성공시 Http Status Code 200(Ok) 반환")
	void getProductsByPagination() throws Exception {
		mockMvc.perform(get("/products")
			.param("page", "1")
			.param("size", "2")
			.contentType(MediaType.APPLICATION_JSON))

			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("상품 수정에 성공할 경우 Http Status Code 201(Created)를 리턴")
	void updateProduct() throws Exception {

		doNothing().when(productService).updateProduct(1L, updateProductRequestDto);

		mockMvc.perform(put("/products/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(updateProductRequestDto))
		)
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("상품 삭제 성공시 Http Status Code 200(Ok) 반환")
	void deleteProduct() throws Exception {

		doNothing().when(productService).deleteProduct(1L);

		mockMvc.perform(delete("/products/{id}", 1)
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk());

	}

	private String toJsonString(Object dto) throws JsonProcessingException {
		return objectMapper.writeValueAsString(dto);
	}

}