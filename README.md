# 상품 서비스 API 서버

상품 서비스 HTTP URL : http://localhost:8081

### Rest Docs를 사용하여 API문서를 자동화 하였습니다.

[API 문서 보기](HyungilJung.github.io)

### 자세한 사항은 Pull Request를 참고해주시면 감사하겠습니다.

[진행 과정](https://github.com/hyungil-msa-project-university/msa-product-service/pulls?q=is%3Apr+is%3Aclosed)

### 디렉토리 구조

```bash
   ├─ main
      ├─ kotlin
         ├─ com.hyungil.productservice
            ├─ domain                                             - 도메인을 담당하는 디렉토리     
                ├─ product                           
                  ├─ api                                          - Controller 같은 경우에는 ModelAndView를 리턴하는 느낌이 있어서 명시적으로 api로 칭함
                      ├─ ProductApi.java
                  ├─ domain                                       - 도메인 엔티티에 대한 클래스로 구성
                      ├─ entity
                          ├─ Product.java
                  ├─ dto                                          - Request, Response 객체들로 구성
                      ├─ request
                          ├─ AddProductrequestDto.java
                          ├─ UpdateProductrequestDto.java
                      ├─ response
                          ├─ GetProductResponseDto.java
                          ├─ GetProductsResponseDto.java
                  ├─ repository                                   - Entity에 의해 생성된 DB에 접근하는 메서드들로 이루어진 클래스로 구성
                      ├─ ProductRepository.java                   
                  ├─ service                                      - 비즈니스 로직을 수행                         
                      ├─ ProductService.java    
               ├─ model                                           - Domain Entity 객체들이 공통적으로 사용할 객체
                  ├─ BaseTimeEntity.java
            
            ├─ global                                             - 프로젝트에서 전역적으로 사용되는 객체들로 구성
                ├─ commom                                         - 공통으로 사용되는 Value 객체들로 구성
                  ├─ util                                         - 유틸성 클래스로 구성
                    ├─ constants                                  - 공통 상수 관련
                        ├─ ErrorMessageConstant.java           
                ├─ error                                          - 예외 핸들링을 담당하는 클래스로 구성
                  ├─ dto                                          - 예외처리 관련 데이터 교환을 위해 사용
                      ├─ ErrorResponserDto.java                              
                  ├─ exception                                    - 예외처리 관련
                      ├─ DuplicateRequestException.java                                
                      ├─ NotFoubdRequestException.java                                
                  ├─ handler                                      - 모든 예외를 한 곳에서 처리하기 위한 클래스로 구성
                      ├─ ProductExceptionHandler.java                             
                             
   ├─ test                                                        -  모든 Layer에 모든 case마다 단위 테스트 작성을 위한 클래스로 구성
      ├─ java
         ├─ com.hyungil.productservice.domain.product
            ├─ api
              ├─ ProductApiTest.java
            ├─ repository
              ├─ ProductRepositoryTest.java
            ├─ service
              ├─ ProductServiceTest.java
                                 
```

### ER 다이어그램

![asd](https://user-images.githubusercontent.com/43127088/135290850-f8276a5b-308b-4d2e-ac59-d3d8351977ab.PNG)

