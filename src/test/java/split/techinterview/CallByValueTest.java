package split.techinterview;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CallByValueTest {

    /**
     * 자바는 메서드를 호출할 때 파라미터를 전달하는 방법으로 Call By Value 를 사용한다.
     *
     * 호출된 함수의 인자는 호출한 함수에서 전달한 변수가 복사되어 있는 변수로 서로 다른 변수입니다.
     * 호출된 함수의 파라미터를 수정해도 호출한 함수가 전달한 변수는 변경되지 않습니다.
     */

    private final Member ako = new Member("ako");

    @Test
    void test() {
        //given
        int number = 0;
        Member member = new Member("split");

        //when
        changeMemberToAkoAndNumberTo10(member, number); //number 를 10으로 변경

        //then
        assertThat(member).isNotEqualTo(ako);
        assertThat(number).isNotEqualTo(10);
    }

    private void changeMemberToAkoAndNumberTo10(Member member, int number) {
        member = ako;
        number = 10;
    }

    private static class Member {

        protected String name;

        public Member(final String name) {
            this.name = name;
        }
    }
}
