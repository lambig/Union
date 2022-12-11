package io.github.lambig.either;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class EitherTest {

    @Nested
    class asJoinedOptional_2FunctionsTest {

        @Test
        void retrieve_optional_of_value() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            Function<Long, Number> toNumber = longValue -> (Number) longValue;
            Function<Object, Integer> toInteger = object -> Integer.parseInt(object.toString());
            //Execute
            Optional<Number> actual = target
                    .asJoinedOptional(
                            toNumber,
                            toInteger);
            //Verify
            assertThat(actual).hasValue(3L);
        }
    }

    @Nested
    class asJoinedOptional_biFunctionTest {
        @Test
        void retrieve_optional_of_value() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            BiFunction<Long, Object, Number> toNumber = (longValue, object) -> (Number) longValue;
            //Execute
            Optional<Number> actual = target.asJoinedOptional(toNumber);
            //Verify
            assertThat(actual).hasValue(3L);
        }
    }

    @Nested
    class PeekWith_2ConsumerTest {
        @Test
        void consume_and_return_self() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            Consumer<Object> objectNoOp = object -> {
            };
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            //Execute
            Either<Long, String> actual = target
                    .peekWith(
                            countUp,
                            objectNoOp);
            //Verify
            assertThat(atomicLong).hasValue(3L);
            assertThat(actual).isEqualTo(target);
        }
    }

    @Nested
    class PeekWith_biConsumerTest {
        @Test
        void consume_and_return_self() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            BiConsumer<Long, Object> countUp = (longValue, object) -> atomicLong.addAndGet(longValue);
            //Execute
            Either<Long, String> actual = target.peekWith(countUp);
            //Verify
            assertThat(atomicLong).hasValue(3L);
            assertThat(actual).isEqualTo(target);
        }
    }

    @Nested
    class PeekLeftWithTest {
        @Test
        void consume_and_return_self() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            //Execute
            Either<Long, String> actual = target.peekLeftWith(countUp);
            //Verify
            assertThat(atomicLong).hasValue(3L);
            assertThat(actual).isEqualTo(target);
        }
    }

    @Nested
    class PeekRightWithTest {
        @Test
        void consume_and_return_self() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            //Execute
            Either<String, Long> actual = target.peekRightWith(countUp);
            //Verify
            assertThat(atomicLong).hasValue(3L);
            assertThat(actual).isEqualTo(target);
        }
    }
}