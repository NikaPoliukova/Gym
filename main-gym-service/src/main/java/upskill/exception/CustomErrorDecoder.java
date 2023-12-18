//package upskill.exception;
//
//import feign.FeignException;
//import feign.Response;
//import feign.codec.ErrorDecoder;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class CustomErrorDecoder implements ErrorDecoder {
//
//  @Override
//  public Exception decode(String methodKey, Response response) {
//    if (response.status() == 503) {
//      log.error("server workload not availeable");
//    }
//    return FeignException.errorStatus(methodKey, response);
//  }
//}
