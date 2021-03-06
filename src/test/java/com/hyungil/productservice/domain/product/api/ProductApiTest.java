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
		.productName("??????")
		.build();

	GetProductResponseDto getProductResponseDto = GetProductResponseDto.builder()
		.productName("??????")
		.build();

	UpdateProductRequestDto updateProductRequestDto = UpdateProductRequestDto.builder()
		.productName("????????????")
		.build();

	GetProductsResponseDto getProductsResponseDto = GetProductsResponseDto.builder()
		.productId(1L).productName("??????").build();

	GetProductsResponseDto getProductsResponseDto2 = GetProductsResponseDto.builder()
		.productId(2L).productName("??????2").build();

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
	@DisplayName("?????? ????????? ????????? ?????? Http Status Code 201(Created)??? ??????")
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
						fieldWithPath("productName").type(JsonFieldType.STRING).description("?????????")
					)
				)
			);
	}


	@Test
	@DisplayName("?????? ????????? Null??? ???????????? ????????? ?????? Http Status Code 400(BadRequest)??? ??????")
	void addProductIfNameIsNull() throws Exception {

		addProductRequestDto = AddProductRequestDto.builder()
			.productName(null)
			.build();

		mockMvc.perform(post("/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(addProductRequestDto))
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("????????? ?????? ???????????????."))
			.andDo(
				document(
					"products/null/fail", getDocumentRequest(), getDocumentResponse(),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")
					),

					requestFields(
						fieldWithPath("productName").type(JsonFieldType.NULL)
							.description("????????? ???????????? null ??? ???")
					)
				)
			);
	}

	@Test
	@DisplayName("?????? ????????? null?????? ??? ??? ?????? ??? ?????? ???????????? ????????? ?????? Http Status Code 400(BadRequest)??? ??????")
	void addProductIfNameEmptyString() throws Exception {

		addProductRequestDto = AddProductRequestDto.builder()
			.productName("")
			.build();

		mockMvc.perform(post("/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(addProductRequestDto))
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("????????? ?????? ???????????????."))
			.andDo(
				document(
					"products/emptu/fail", getDocumentRequest(), getDocumentResponse(),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")
					),

					requestFields(
						fieldWithPath("productName").type(JsonFieldType.STRING)
							.description("????????? ???????????? ????????? ???")
					)
				)
			);
	}

	@Test
	@DisplayName("?????? ????????? null?????? ????????? ???????????? ????????? ?????? Http Status Code 400(BadRequest)??? ??????")
	void addProductIfNameIsBlankString() throws Exception {

		addProductRequestDto = AddProductRequestDto.builder()
			.productName(" ")
			.build();

		mockMvc.perform(post("/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(toJsonString(addProductRequestDto))
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("????????? ?????? ???????????????."))
			.andDo(
				document(
					"products/blank/fail", getDocumentRequest(), getDocumentResponse(),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")
					),

					requestFields(
						fieldWithPath("productName").type(JsonFieldType.STRING)
							.description("????????? ???????????? ????????????")
					)
				)
			);
	}

	@Test
	@DisplayName("?????? ?????? ????????? ????????? ?????? Http Status Code 201(Created)??? ??????")
	void getProduct() throws Exception {

		given(productService.getProduct(1L)).willReturn(getProductResponseDto);

		mockMvc.perform(RestDocumentationRequestBuilders.get("/products/{id}", 1)
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productName").value("??????"))
			.andDo(
				document(
					"products/get/successful", getDocumentRequest(), getDocumentResponse(),

					pathParameters(parameterWithName("id").description("????????? ????????? ??????")),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")
					),

					responseFields(
						fieldWithPath("productName").type(JsonFieldType.STRING)
							.description("????????? ?????????")
					)
				)
			);
	}

	@Test
	@DisplayName("?????? ?????? ?????? ????????? Http Status Code 200(Ok) ??????")
	void getProductsByPagination() throws Exception {

		given(productService.getProductsByPagination(any())).willReturn(productList);

		mockMvc.perform(get("/products")
			.param("page", "0")
			.param("size", "2")
			.contentType(MediaType.APPLICATION_JSON))

			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].productId").value("1"))
			.andExpect(jsonPath("$.[0].productName").value("??????"))
			.andExpect(jsonPath("$.[1].productId").value("2"))
			.andExpect(jsonPath("$.[1].productName").value("??????2"))
			.andDo(
				document(
					"products/all/get/successful", getDocumentRequest(), getDocumentResponse(),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")
					),

					requestParameters(
						parameterWithName("page").description("????????? ???????????? ?????????").optional(),
						parameterWithName("size").description("????????? ???????????? ???????????? ????????? ???").optional()
					),

					responseFields(
						fieldWithPath("[].productId").type(JsonFieldType.NUMBER)
							.description("?????? ??????"),
						fieldWithPath("[].productName").type(JsonFieldType.STRING)
							.description("????????? ?????????")
					)
				)
			);
	}

	@Test
	@DisplayName("?????? ????????? ????????? ?????? Http Status Code 201(Created)??? ??????")
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

					pathParameters(parameterWithName("id").description("????????? ????????? ??????")),

					requestHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type")),

					requestFields(
						fieldWithPath("productName").type(JsonFieldType.STRING)
							.description("????????? ?????????")
					)
				)
			);
	}

	@Test
	@DisplayName("?????? ?????? ????????? Http Status Code 200(Ok) ??????")
	void deleteProduct() throws Exception {

		doNothing().when(productService).deleteProduct(1L);

		mockMvc.perform(RestDocumentationRequestBuilders.delete("/products/{id}", 1)
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andDo(
				document(
					"products/delete/successful",

					pathParameters(parameterWithName("id").description("????????? ????????? ??????")),

					pathParameters(
						parameterWithName("id").description("????????? ????????? ??????")
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