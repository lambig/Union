package io.github.lambig.union;

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

class NeitherTest {
    @Nested
    class leftTest {
        @Test
        void throws_exception() {
            //SetUp
            Union<String, Long> target = UnionOf.none();
            //Exercise
            assertThatThrownBy(target::left)
                    //Verify
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Neither has been requested to return left value");
        }
    }

    @Nested
    class rightTest {
        @Test
        void throws_exception() {
            //SetUp
            Union<String, Long> target = UnionOf.none();
            //Exercise
            assertThatThrownBy(target::right)
                    //Verify
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Neither has been requested to return right value");
        }
    }

    @Nested
    class rightOptionalTest {
        @Test
        void retrieve_Optional() {
            //SetUp
            Union<String, Long> target = UnionOf.none();
            //Exercise
            Optional<Long> actual = target.rightOptional();
            //Verify
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class leftOptionalTest {
        @Test
        void retrieve_Optional() {
            //SetUp
            Union<String, Long> target = UnionOf.none();
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
            Union<String, Long> target = UnionOf.none();
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
            Union<String, Long> target = UnionOf.none();
            //Exercise
            boolean actual = target.hasRight();
            //Verify
            assertThat(actual).isFalse();
        }
    }

    @Nested
    class asJoined_2FunctionsTest {
        @Test
        void retrieve_value() {
            //SetUp
            Union<String, Long> target = UnionOf.none();
            Function<Long, Number> toNumber = longValue -> (Number) longValue;
            Function<Object, Integer> toInteger = object -> Integer.parseInt(object.toString());
            //Exercise
            Number actual = target
                    .asJoined(
                            toInteger,
                            toNumber);
            //Verify
            assertThat(actual).isNull();
        }
    }

    @Nested
    class asJoined_BiFunctionTest {
        @Test
        void retrieve_value() {
            //SetUp
            Union<String, Long> target = UnionOf.none();
            BiFunction<Object, Long, Number> toNumber = (object, longValue) -> (Number) longValue;
            //Exercise
            Number actual = target.asJoined(toNumber);
            //Verify
            assertThat(actual).isNull();
        }
    }

    @Nested
    class acceptWith_2ConsumersTest {
        @Test
        void invocation_check() {
            //SetUp
            Union<String, Long> target = UnionOf.none();
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
            assertThat(atomicLong).hasValue(0L);
        }
    }

    @Nested
    class acceptWith_BiConsumerTest {
        @Test
        void invocation_check() {
            //SetUp
            Union<String, Long> target = UnionOf.none();
            AtomicInteger atomicInteger = new AtomicInteger(0);
            BiConsumer<Object, Number> countUp =
                    (object, number) -> atomicInteger.addAndGet(Integer.parseInt(Objects.toString(number)));
            //Exercise
            target.accept(countUp);
            //Verify
            assertThat(atomicInteger).hasValue(0);
        }
    }

    @Nested
    class acceptRightWithTest {
        @Test
        void invocation_check() {
            //SetUp
            Union<String, Long> target = UnionOf.none();
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            //Exercise
            target.acceptRight(countUp);
            //Verify
            assertThat(atomicLong).hasValue(0L);
        }
    }

    @Nested
    class acceptLeftWithTest {
        @Test
        void invocation_check_do_nothing() {
            //SetUp
            Union<String, Long> target = UnionOf.none();
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<String> countUp = nothing -> atomicLong.incrementAndGet();
            //Exercise
            target.acceptLeft(countUp);
            //Verify
            assertThat(atomicLong).hasValue(0L);
        }
    }
}