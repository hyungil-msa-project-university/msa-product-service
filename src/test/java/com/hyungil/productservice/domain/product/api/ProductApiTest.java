package com.hyungil.productservice.domain.product.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
import com.hyungil.productservice.domain.product.dto.request.UpdateProductRequestDto;
import com.hyungil.productservice.domain.product.dto.response.GetProductResponseDto;
import com.hyungil.productservice.domain.product.dto.response.GetProductsResponseDto;
import com.hyungil.productservice.domain.product.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductApi.class)
@ExtendWith(RestDocumentationExtension.class)
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

	AddProductRequestDto addProductRequestDto = AddProductRequestDto.builder()
		.productName("상품")
		.build();

	GetProductResponseDto getProductResponseDto = GetProductResponseDto.builder()
		.productName("상품")
		.build();

	UpdateProductRequestDto updateProductRequestDto = UpdateProductRequestDto.builder()
		.productName("상품상품")
		.build();

	GetProductsResponseDto getProductsResponseDto = GetProductsResponseDto.builder()
		.productId(1L).productName("상품").build();

	GetProductsResponseDto getProductsResponseDto2 = GetProductsResponseDto.builder()
		.productId(2L).productName("상품2").build();

	List<GetProductsResponseDto> productList = new ArrayList<>();

	@BeforeEach
	void setUp(RestDocumentationContextProvider restDocumentation) {

		mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.apply(documentationConfiguration(restDocumentation))
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.alwaysDo(print())
			.build();

		productList.add(getProductsResponseDto);
		productList.add(getProductsResponseDto2);
	}

	@Test
	@DisplayName("상품 등록에 성공할 경우 Http Status Code 201(Created)를 리턴")
	void addProduct() throws Exception {

		doNothing().when(productService).addProduct(addProductRequestDto);

		mockMvc.perform(post("/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(addProductRequestDto))
		)
			.andExpect(status().isCreated())
			.andDo(
				document(
					"products/create/successful", getDocumentRequest(), getDocumentResponse(),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")
					),

					requestFields(
						fieldWithPath("productName").type(JsonFieldType.STRING).description("상품명")
					)
				)
			);
	}


	@Test
	@DisplayName("상품 이름에 Null을 등록하여 실패한 경우 Http Status Code 400(BadRequest)를 리턴")
	void addProductIfNameIsNull() throws Exception {

		addProductRequestDto = AddProductRequestDto.builder()
			.productName(null)
			.build();

		mockMvc.perform(post("/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(addProductRequestDto))
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("잘못된 상품 이름입니다."))
			.andDo(
				document(
					"products/null/fail", getDocumentRequest(), getDocumentResponse(),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")
					),

					requestFields(
						fieldWithPath("productName").type(JsonFieldType.NULL)
							.description("요청한 상품명이 null 일 때")
					)
				)
			);
	}

	@Test
	@DisplayName("상품 이름에 null이나 빈 값 또는 공 백을 등록하여 실패한 경우 Http Status Code 400(BadRequest)를 리턴")
	void addProductIfNameEmptyString() throws Exception {

		addProductRequestDto = AddProductRequestDto.builder()
			.productName("")
			.build();

		mockMvc.perform(post("/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(addProductRequestDto))
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("잘못된 상품 이름입니다."))
			.andDo(
				document(
					"products/emptu/fail", getDocumentRequest(), getDocumentResponse(),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")
					),

					requestFields(
						fieldWithPath("productName").type(JsonFieldType.STRING)
							.description("요청한 상품명이 빈값일 떼")
					)
				)
			);
	}

	@Test
	@DisplayName("상품 이름에 null이나 공백을 등록하여 실패한 경우 Http Status Code 400(BadRequest)를 리턴")
	void addProductIfNameIsBlankString() throws Exception {

		addProductRequestDto = AddProductRequestDto.builder()
			.productName(" ")
			.build();

		mockMvc.perform(post("/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(addProductRequestDto))
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("잘못된 상품 이름입니다."))
			.andDo(
				document(
					"products/blank/fail", getDocumentRequest(), getDocumentResponse(),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")
					),

					requestFields(
						fieldWithPath("productName").type(JsonFieldType.STRING)
							.description("요청한 상품명이 공백일때")
					)
				)
			);
	}

	@Test
	@DisplayName("특정 상품 조회에 성공할 경우 Http Status Code 201(Created)를 리턴")
	void getProduct() throws Exception {

		given(productService.getProduct(1L)).willReturn(getProductResponseDto);

		mockMvc.perform(RestDocumentationRequestBuilders.get("/products/{id}", 1)
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productName").value("상품"))
			.andDo(
				document(
					"products/get/successful", getDocumentRequest(), getDocumentResponse(),

					pathParameters(parameterWithName("id").description("조회할 상품의 번호")),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")
					),

					responseFields(
						fieldWithPath("productName").type(JsonFieldType.STRING)
							.description("조회한 상품명")
					)
				)
			);
	}

	@Test
	@DisplayName("전체 상품 조회 성공시 Http Status Code 200(Ok) 반환")
	void getProductsByPagination() throws Exception {

		given(productService.getProductsByPagination(any())).willReturn(productList);

		mockMvc.perform(get("/products")
			.param("page", "0")
			.param("size", "2")
			.contentType(MediaType.APPLICATION_JSON))

			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].productId").value("1"))
			.andExpect(jsonPath("$.[0].productName").value("상품"))
			.andExpect(jsonPath("$.[1].productId").value("2"))
			.andExpect(jsonPath("$.[1].productName").value("상품2"))
			.andDo(
				document(
					"products/all/get/successful", getDocumentRequest(), getDocumentResponse(),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")
					),

					requestParameters(
						parameterWithName("page").description("조회할 게시글의 페이지").optional(),
						parameterWithName("size").description("조회할 게시글의 페이지당 게시글 수").optional()
					),

					responseFields(
						fieldWithPath("[].productId").type(JsonFieldType.NUMBER)
							.description("상품 번호"),
						fieldWithPath("[].productName").type(JsonFieldType.STRING)
							.description("조회한 상품명")
					)
				)
			);
	}

	@Test
	@DisplayName("상품 수정에 성공할 경우 Http Status Code 201(Created)를 리턴")
	void updateProduct() throws Exception {

		doNothing().when(productService).updateProduct(1L, updateProductRequestDto);

		mockMvc.perform(put("/products/{id}", 1)
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(updateProductRequestDto))
		)
			.andExpect(status().isCreated())
			.andDo(
				document(
					"products/update/successful", getDocumentRequest(), getDocumentResponse(),

					pathParameters(parameterWithName("id").description("수정할 상품의 번호")),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")),

					requestFields(
						fieldWithPath("productName").type(JsonFieldType.STRING)
							.description("수정할 상품명")
					)
				)
			);
	}

	@Test
	@DisplayName("상품 삭제 성공시 Http Status Code 200(Ok) 반환")
	void deleteProduct() throws Exception {

		doNothing().when(productService).deleteProduct(1L);

		mockMvc.perform(RestDocumentationRequestBuilders.delete("/products/{id}", 1)
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andDo(
				document(
					"products/delete/successful",

					pathParameters(parameterWithName("id").description("삭제할 상품의 번호")),

					pathParameters(
						parameterWithName("id").description("삭제할 상품의 번호")
					)
				)
			);
	}

	private OperationRequestPreprocessor getDocumentRequest() {
		return preprocessRequest(modifyUris().scheme("http").host("localhost").port(8081),
			prettyPrint());
	}

	private OperationResponsePreprocessor getDocumentResponse() {
		return preprocessResponse(prettyPrint());
	}

	private String toJsonString(Object dto) throws JsonProcessingException {
		return objectMapper.writeValueAsString(dto);
	}

}