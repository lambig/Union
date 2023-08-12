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

class LeftTest {
    @Nested
    class initializeTest {
        @Test
        void reject_nullValue() {
            //SetUp
            //Exercise
            assertThatThrownBy(() -> UnionOf.left(null))
                    //Verify
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("left is marked non-null but is null");
        }
    }

    @Nested
    class leftTest {
        @Test
        void retrieve_value() {
            //SetUp
            Union<Long, String> target = UnionOf.left(3L);
            //Exercise
            Long actual = target.left();
            //Verify
            assertThat(actual).isEqualTo(3L);
        }
    }

    @Nested
    class rightTest {
        @Test
        void throws_exception() {
            //SetUp
            Union<Long, String> target = UnionOf.left(3L);
            //Exercise
            assertThatThrownBy(target::right)
                    //Verify
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Left value of Union has been requested to return right value");
        }
    }

    @Nested
    class leftOptionalTest {
        @Test
        void retrieve_Optional() {
            //SetUp
            Union<Long, String> target = UnionOf.left(3L);
            //Exercise
            Optional<Long> actual = target.leftOptional();
            //Verify
            assertThat(actual).hasValue(3L);
        }
    }

    @Nested
    class rightOptionalTest {
        @Test
        void retrieve_Optional() {
            //SetUp
            Union<Long, String> target = UnionOf.left(3L);
            //Exercise
            Optional<String> actual = target.rightOptional();
            //Verify
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class hasLeftTest {
        @Test
        void returns_true() {
            //SetUp
            Union<Long, String> target = UnionOf.left(3L);
            //Exercise
            boolean actual = target.hasLeft();
            //Verify
            assertThat(actual).isTrue();
        }
    }

    @Nested
    class hasRightTest {
        @Test
        void returns_false() {
            //SetUp
            Union<Long, String> target = UnionOf.left(3L);
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
            Union<Long, String> target = UnionOf.left(3L);
            Function<Long, Number> toNumber = longValue -> (Number) longValue;
            Function<Object, Integer> toInteger = object -> Integer.parseInt(object.toString());
            //Exercise
            Number actual = target
                    .asJoined(
                            toNumber,
                            toInteger);
            //Verify
            assertThat(actual).isEqualTo(3L);
        }
    }

    @Nested
    class asJoined_BiFunctionTest {
        @Test
        void retrieve_value() {
            //SetUp
            Union<Long, String> target = UnionOf.left(3L);
            BiFunction<Long, Object, Number> toNumber = (longValue, object) -> (Number) longValue;
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
            Union<Long, String> target = UnionOf.left(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            Consumer<Object> objectNoOp = object -> {
            };
            //Exercise
            target
                    .accept(
                            countUp,
                            objectNoOp);
            //Verify
            assertThat(atomicLong).hasValue(3L);
        }
    }

    @Nested
    class acceptWith_BiConsumerTest {
        @Test
        void invocation_check() {
            //SetUp
            Union<Long, String> target = UnionOf.left(3L);
            AtomicInteger atomicInteger = new AtomicInteger(0);
            BiConsumer<Number, Object> countUp =
                    (number, object) -> atomicInteger.addAndGet(Integer.parseInt(Objects.toString(number)));
            //Exercise
            target.accept(countUp);
            //Verify
            assertThat(atomicInteger).hasValue(3);
        }
    }

    @Nested
    class acceptLeftWithTest {
        @Test
        void invocation_check() {
            //SetUp
            Union<Long, String> target = UnionOf.left(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            //Exercise
            target.acceptLeft(countUp);
            //Verify
            assertThat(atomicLong).hasValue(3);
        }
    }

    @Nested
    class acceptRightWithTest {
        @Test
        void invocation_check_do_nothing() {
            //SetUp
            Union<Long, String> target = UnionOf.left(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<String> countUp = nothing -> atomicLong.incrementAndGet();
            //Exercise
            target.acceptRight(countUp);
            //Verify
            assertThat(atomicLong).hasValue(0L);
        }
    }

    @Nested
    class getTest {
        @Test
        void retrieve_value() {
            //SetUp
            Left<Long, String> target = UnionOf.left(3L);
            //Exercise
            Long actual = target.get();
            //Verify
            assertThat(actual).isEqualTo(3L);
        }
    }
}