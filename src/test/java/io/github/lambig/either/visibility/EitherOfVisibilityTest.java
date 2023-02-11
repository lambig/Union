package io.github.lambig.either.visibility;

import io.github.lambig.either.Either;
import io.github.lambig.either.EitherOf;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EitherOfVisibilityTest {

    @Nested
    class leftTest {
        @Test
        void retrieve_instance() {
            Either<Long, String> target = EitherOf.left(3L);
            assertThat(target.left()).isEqualTo(3L);
            assertThatThrownBy(target::right)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Left value of Either has been requested to return right value");
        }
    }

    @Nested
    class rightTest {
        @Test
        void retrieve_instance() {
            Either<Long, String> target = EitherOf.right("abc");
            assertThat(target.right()).isEqualTo("abc");
            assertThatThrownBy(target::left)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Right value of Either has been requested to return left value");
        }
    }

    @Nested
    class noneTest {
        @Test
        void retrieve_instance() {
            Either<Long, String> target = EitherOf.none();
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
            Either<Long, String> target = EitherOf.l(3L).<String>r();
            assertThat(target.left()).isEqualTo(3L);
            assertThatThrownBy(target::right)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Left value of Either has been requested to return right value");
        }
    }


    @Nested
    class rTest {
        @Test
        void retrieve_instance() {
            Either<Long, String> target = EitherOf.r("abc").<Long>l();
            assertThat(target.right()).isEqualTo("abc");
            assertThatThrownBy(target::left)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Right value of Either has been requested to return left value");
        }
    }


}