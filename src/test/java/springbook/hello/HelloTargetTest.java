package springbook.hello;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;


class HelloTargetTest {

    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby"), equals("Hello Toby"));
    }

    @Test
    void test_1(){
        Hello proxiedHello = new HelloUppercase(new HelloTarget());
        assertThat(proxiedHello.sayHello("Toby"), equals("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby"), equals("HI TOBY"));
    }

}