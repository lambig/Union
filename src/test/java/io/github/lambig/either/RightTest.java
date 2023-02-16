package io.github.lambig.either;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class RightTest {
    @Nested
    class initializeTest {
        @Test
        void reject_nullValue() {
            //SetUp
            //Exercise
            assertThatThrownBy(() -> EitherOf.right(null))
                    //Verify
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("right is marked non-null but is null");
        }
    }

    @Nested
    class rightTest {
        @Test
        void retrieve_value() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            //Exercise
            Long actual = target.right();
            //Verify
            assertThat(actual).isEqualTo(3L);
        }
    }

    @Nested
    class leftTest {
        @Test
        void throws_exception() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            //Exercise
            assertThatThrownBy(target::left)
                    //Verify
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Right value of Either has been requested to return left value");
        }
    }

    @Nested
    class rightOptionalTest {
        @Test
        void retrieve_Optional() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            //Exercise
            Optional<Long> actual = target.rightOptional();
            //Verify
            assertThat(actual).hasValue(3L);
        }
    }

    @Nested
    class leftOptionalTest {
        @Test
        void retrieve_Optional() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            //Exercise
            Optional<String> actual = target.leftOptional();
            //Verify
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class hasLeftTest {
        @Test
        void returns_false() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            //Exercise
            boolean actual = target.hasLeft();
            //Verify
            assertThat(actual).isFalse();
        }
    }

    @Nested
    class hasRightTest {
        @Test
        void returns_true() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            //Exercise
            boolean actual = target.hasRight();
            //Verify
            assertThat(actual).isTrue();
        }
    }

    @Nested
    class asJoined_2FunctionsTest {
        @Test
        void retrieve_value() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            Function<Long, Number> toNumber = longValue -> (Number) longValue;
            Function<Object, Integer> toInteger = object -> Integer.parseInt(object.toString());
            //Exercise
            Number actual = target
                    .asJoined(
                            toInteger,
                            toNumber);
            //Verify
            assertThat(actual).isEqualTo(3L);
        }
    }

    @Nested
    class asJoined_BiFunctionTest {
        @Test
        void retrieve_value() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            BiFunction<Object, Long, Number> toNumber = (object, longValue) -> (Number) longValue;
            //Exercise
            Number actual = target.asJoined(toNumber);
            //Verify
            assertThat(actual).isEqualTo(3L);
        }
    }

    @Nested
    class acceptWith_2ConsumersTest {
        @Test
        void invocation_check() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            Consumer<Object> objectNoOp = object -> {
            };
            //Exercise
            target
                    .accept(
                            objectNoOp,
                            countUp);
            //Verify
            assertThat(atomicLong).hasValue(3L);
        }
    }

    @Nested
    class acceptWith_BiConsumerTest {
        @Test
        void invocation_check() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            AtomicInteger atomicInteger = new AtomicInteger(0);
            BiConsumer<Object, Number> countUp =
                    (object, number) -> atomicInteger.addAndGet(Integer.parseInt(Objects.toString(number)));
            //Exercise
            target.accept(countUp);
            //Verify
            assertThat(atomicInteger).hasValue(3);
        }
    }

    @Nested
    class acceptRightWithTest {
        @Test
        void invocation_check() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            //Exercise
            target.acceptRight(countUp);
            //Verify
            assertThat(atomicLong).hasValue(3);
        }
    }

    @Nested
    class acceptLeftWithTest {
        @Test
        void invocation_check_do_nothing() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<String> countUp = nothing -> atomicLong.incrementAndGet();
            //Exercise
            target.acceptLeft(countUp);
            //Verify
            assertThat(atomicLong).hasValue(0L);
        }
    }

    @Nested
    class getTest {
        @Test
        void retrieve_value() {
            //SetUp
            Right<String, Long> target = EitherOf.right(3L);
            //Exercise
            Long actual = target.get();
            //Verify
            assertThat(actual).isEqualTo(3L);
        }
    }
}