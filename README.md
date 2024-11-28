## 서비스명
- 인포쉐어
### 서비스 설명
- 카페와 블로그의 장점을 합친 서비스
- 주요기능 : 카페, 블로그(개인페이지), 이웃, 태그

<br />
<br/>


## 🛠 개발환경
- JAVA
- Spring
- SpringBoot
- Thymeleaf
- JPA
- HTML/CSS
- JavaScript
- JQuery
- MariaSQL

<br/>
<br/>

## ☁️ ERD

![Untitled](https://github.com/user-attachments/assets/71599c3d-a1a5-458a-bc4d-063caa7723d6)

<br>
<br>

## 👀 시연영상
[![image](https://github.com/user-attachments/assets/21e2aa44-66ac-4880-9ee8-93bf24cb0fc9)](https://youtu.be/dMoc0QNOYU8)
*이미지를 클릭하면 영상 링크로 이동합니다. 

<br>
<br>

## 🔥 트러블 슈팅

### 🚨 Issue 1
### 🚧 카카오 AJAX 구현
A. 이슈 내역
카카오 로그인 api를 AJAX로 구현 시, 최초 로그인 이후 카카오로 로그인을 다시 하려고 하면 생기는 로그인 오류가 있었다. 
### 🛑 원인
- AJAX 비동기 요청에 대해 쿠키, 캐시와 같은 브라우저 데이터가 남게된다. 이는 로그인 정보를 다시 받는데 있어서 방해가 된다.
### 🚥 해결
  로그인 요청을 AJAX가 아니라 a태그 href로 처리하였다.
  
<br>
<br>

### 🚨 Issue 2
### 🚧 @PreAuthorize("isAuthenticated()")가 있는 메소드를 비로그인 상태에서 AJAX로 처리할 때 발생 오류
A. 이슈 내역
로그인 상태에서 추천 기능을 문제없이 잘 사용하다가, 비로그인 상태에서 추천 버튼을 누르니 html파일의 텍스트가 리턴값으로 넘어오는 것을 확인하였다. 
### 🛑 원인
- @PreAuthorize("isAuthenticated()")가 적용된 메소드를 보통 get이나, post로 페이지 요청하면 리다이렉트 시킨다.
- AJAX 비동기 요청은 리다이렉트의 목적지를 리턴시켜 html 텍스트가 요청의 리턴값으로 들어오도록 되었다.
### 🚥 해결
- AJAX 리턴값에 html의 xml 마크업 언어가 포함될 시 js로 직접 로그인 페이지로 이동을 시켜주었다.

<br>
<br>

### 🚨 Issue 3
### 🚧 리다이렉트에 한글 경로가 있을 경우
A. 이슈 내역
리다이렉트 경로에 한글 경로가 있으면 302 리다이렉트 에러가 나면서 빈페이지가 뜬 채로 페이지 이동이 되지 않는다. 
### 🛑 원인
- 스프링부트는 리다이렉트 시킬때 아스키 코드의 문자는 그대로 반영되지만, 비아스키 코드의 문자는 UTF-8 형식으로 인코딩되어 반영된다.
### 🚥 해결
- 한글 경로를 영어 경로로 변경시켜주었다.
- (다른 방법) 한글 경로를 UTF-8로 인코딩 시켜서 경로에 포함시켜준다.
