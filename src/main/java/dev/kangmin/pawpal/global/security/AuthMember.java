package dev.kangmin.pawpal.global.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)// 런타임일 떄도 유효( 해당 어노테이션 없을 시 컴파일 시에만 유효 )

//인증 객체 정보를 가지는 커스텀 어노테이션.
//AuthDetails의 필드 member를 가져옴.
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : member")
public @interface AuthMember {
}
