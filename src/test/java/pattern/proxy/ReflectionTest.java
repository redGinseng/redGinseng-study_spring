package pattern.proxy;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class ReflectionTest {

    interface Hello {

        String sayHello(String name);

        String sayHi(String name);

        String sayThankYou(String name);
    }

    public class HelloTarget implements Hello {

        @Override
        public String sayHello(String name) {
            return "Hello " + name;
        }

        @Override
        public String sayHi(String name) {
            return "Hi " + name;
        }

        @Override
        public String sayThankYou(String name) {
            return "Thank you " + name;
        }
    }

    public class HelloUpperCase implements Hello {

        Hello hello; //타겟 오브젝트


        public HelloUpperCase(Hello hello) {
            this.hello = hello;
        }


        @Override
        public String sayHello(String name) {
            return hello.sayHello(name).toUpperCase();
        }

        @Override
        public String sayHi(String name) {
            return hello.sayHi(name).toUpperCase();
        }

        @Override
        public String sayThankYou(String name) {
            return hello.sayThankYou(name).toUpperCase();
        }
    }


    @Test
    public void testHelloUpperCaseProxy() {
        Hello proxyHello = new HelloUpperCase(new HelloTarget());
        assertThat(proxyHello.sayHello("Toby"), equalTo("HELLO TOBY"));
        assertThat(proxyHello.sayHi("Toby"), equalTo("HI TOBY"));
        assertThat(proxyHello.sayThankYou("Toby"), equalTo("THANK YOU TOBY"));
    }

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby"), equalTo("Hello Toby"));
        assertThat(hello.sayHi("Toby"), equalTo("Hi Toby"));
        assertThat(hello.sayThankYou("Toby"), equalTo("Thank you Toby"));
    }

    @Test
    public void invokeMethod() throws Exception {

        String name = "Spring";

        assertThat(name.length(), equalTo(6));

        // Java reflect를 이용해서, 클래스의 메타정보를 가져와서 메소드 호출
        Method lengthMethod = String.class.getMethod("length");
        assertThat((Integer) lengthMethod.invoke(name), equalTo(6));
    }

}