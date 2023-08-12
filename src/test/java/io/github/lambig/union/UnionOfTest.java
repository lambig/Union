package io.github.lambig.union;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UnionOfTest {

    @Nested
    class leftTest {
        @Test
        void retrieve_instance() {
            Union<Long, String> target = UnionOf.left(3L);
            assertThat(target.left()).isEqualTo(3L);
            assertThatThrownBy(target::right)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Left value of Union has been requested to return right value");
        }
    }

    @Nested
    class rightTest {
        @Test
        void retrieve_instance() {
            Union<Long, String> target = UnionOf.right("abc");
            assertThat(target.right()).isEqualTo("abc");
            assertThatThrownBy(target::left)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Right value of Union has been requested to return left value");
        }
    }

    @Nested
    class noneTest {
        @Test
        void retrieve_instance() {
            Union<Long, String> target = UnionOf.none();
            assertThatThrownBy(target::left)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Neither has been requested to return left value");
            assertThatThrownBy(target::right)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Neither has been requested to return right value");
        }
    }

    @Nested
    class lTest {
        @Test
        void retrieve_instance() {
            Union<Long, String> target = UnionOf.l(3L).<String>r();
            assertThat(target.left()).isEqualTo(3L);
            assertThatThrownBy(target::right)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Left value of Union has been requested to return right value");
        }
    }


    @Nested
    class rTest {
        @Test
        void retrieve_instance() {
            Union<Long, String> target = UnionOf.r("abc").<Long>l();
            assertThat(target.right()).isEqualTo("abc");
            assertThatThrownBy(target::left)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Right value of Union has been requested to return left value");
        }
    }


}