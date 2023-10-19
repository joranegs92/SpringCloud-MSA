package com.example.userservice.security;


import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UserService userService;
	private Environment env;

	public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
		super(authenticationManager);
		this.userService = userService;
		this.env = env;
	}
	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws AuthenticationException
	 */

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
												HttpServletResponse response) throws AuthenticationException {
		try {
			RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
			// request.getInputStream() 받은 이유는 request가 포스트형태로 받아지기때문에 inputStream으로 받으면 어떤값이 받아졌는지 확인할 수 있다.


			/*
			* Authentication manager가 authenticate 메소드로 Authentication으로 변경해주고
			* 사용자가 입력했던 스프링 시큐리티에서 사용할수 있는 이메일과 패스워드의 값으로 변경해주기 위해
			* UsernamePasswordAuthenticationToken을 사용한다.
			* 마지막 new ArrayList<>는 권한에 관련된 인자값이다,
			*
			* 이로 인증처리를 한다.
			* */
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getEmail(),
							creds.getPassword(),
							new ArrayList<>()
					)
			);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 로그인 성공시 어떠한 처리를 해줄것인지에 대한 메소드 (토큰시간, 사용자가 로그인했을때 어떤반환값? 등)
	 * @param request
	 * @param response
	 * @param chain
	 * @param authResult
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
	}

/*
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String userName = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(userName);

        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());
    }*/
}
