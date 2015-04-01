#### 1. Tomcat 서버를 시작할 때 웹 애플리케이션이 초기화하는 과정을 설명하라.
* WAS가 시작되면 처음에 Deployed Descriptor 문서 (일반적으로 web.xml) 로 접근하여 초기화 파라미터를 로드하여 전역변수와 같이 모든 Servlet 에서 공유할 수 있는 Servlet Context 인스턴스를 생성한다. 다만 servlet 초기화 파라미터의 경우 해당 서블릿에서만 사용할 수 있으며, 해당 서블릿이 init 될 때 전달받게 된다.   
web.xml은 특별한 설정이 없다면 base directory인 webapp 또는 WebContent 하위 WEB-INF 에 존재해야 한다.  
또한 Servlet Request가 발생했을때 요구하는 Servlet의 매핑정보를 web.xml에서 불러오는데 이 정보에 따래 <url-pattern>으로 지정된 url로 HTTP Request가 발생할 경우 해당 Servlet을 호출하게 된다. 
Servlet3.0 부터는 web.xml 이외에 WebServlet Annotation 방식으로 매핑이 이루어질 수 있고 해당 소스코드는 WebServlet Annotation 방식으로 이루어져 있다.  
이 과정을 거치면 서블릿 컨테이너는 ServletContextEvent를 발생시키는데 만약 ServletContextListener 라는 리스너 인터페이스를 구현하는 서블릿클래스를 web.xml에 리스너클래스(Listener-class)로 등록하게 되면 ServletContextEvent가 발생했을때 해당 클래스의 contextInitialized 메서드가 실행되고 반대로 서블릿 컨테이너가 종료되면 contextDestoryed 메서드가 실행된다.  
이 소스코드에서는 next.support.context 에 ContextLoaderListener 라는 클래스로 구현되어 있으며 서비스에서 사용될 데이터베이스를 초기화하는 로직을 담고있다.  
이후에는 web.xml 또는 WebServlet Annotation에 loadOnStartup이 1(true) 로 설정된 서블릿에 대해 최초 적재가 이루어진다. 이 소스코드에서는 DispatcherServlet이 loadOnStartup으로 설정되어 해당 클래스의 init메서드가 실행된다.  
위의 모든 과정을 마치고 이제 WAS는 클라이언트의 요청을 기다리는 상태가 되었다.  
  
  
#### 2. Tomcat 서버를 시작한 후 http://localhost:8080으로 접근시 호출 순서 및 흐름을 설명하라.
* 클라이언트에서 localhost:8080 을 요청하면 WAS는 index.jsp에 해당하는 Servlet인 index_jsp.java 를 index_jsp.class로 컴파일하고 실행하는데 이 과정에서 index.class에 포함된 response.sendRedirect("/list.next") 에 따라 클라이언트에게 list.next 로 Redirect 하라는 Response를 준다. 그 결과 클라이언트는 /list.next 인 url로 다시 요청하고 WAS에서는 WebServlet Annotation 이 .next 로 지정된 DispatcherServlet 클래스가 이 요청을 받는다.  
DispatcherServlet은 urlExceptParameter 메서드를 통해 url 파라미터를 제거한 url을 인자로 RequestMapping의 인스턴스 객체인 rm의 정적메서드인 findController를 통해 Controller 인스턴스를 반환받는다.    
여기에서는 list.next를 인자로 받기 때문에 listController 라는 Controller를 받게된다.  
이제 받은 Controller에서 클라이언트에게 보내줄 Model과 View에 대한 정보를 받을 수 있는 execute() 메서드를 실행한다. 
execute() 메서드에서는 화면에 보여줄 View로 list.jsp를, Model로 QuestionDao의 findAll() 메서드로 모든 질문데이터를 ModelAndView 인스턴스에 추가해준다.  
다시 이 ModelAndView를 받은 Controller에서는 getView() 메서드로 View 객체를 받고, View의 render() 메서드를 실행한다.  
render() 메서드는 데이터로 사용될 Model을 인자로 받아 세션에 넣어서 View name에 해당하는 jsp로 Request Dispatcher Forwarding을 수행한다.  
지금 과정에서는 질문목록리스트를 questions라는 key를 가진 세션으로 담아 list.jsp로 Forwarding하게 된다.  
마시막으로 list.jsp(정확하게는 list_jsp.class)에서 jstl태그에 의해 questions에 담긴 질문만큼 post 엘레먼트를 생성하고 이렇게 만들어진 page를 클라이언트가 받게 된다.

#### 8. ListController와 ShowController가 멀티 쓰레드 상황에서 문제가 발생하는 이유에 대해 설명하라.
* 

