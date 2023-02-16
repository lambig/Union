package io.github.lambig.either;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.lambig.either.Either.*;
import static org.assertj.core.api.Assertions.assertThat;

class EitherTest {

    @Nested
    class toJoinedWith_2FunctionsTest {

        @Test
        void retrieve_value() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            Function<Long, Number> asNumber = longValue -> (Number) longValue;
            Function<Object, Integer> asInteger = object -> Integer.parseInt(object.toString());
            //Execute
            Optional<Number> actual =
                    Optional.of(target)
                            .map(
                                    toJoinedWith(
                                            asNumber,
                                            asInteger
                                    ));
            //Verify
            assertThat(actual).hasValue(3L);
        }
    }

    @Nested
    class toJoinedWith_biFunctionTest {
        @Test
        void retrieve_value() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            BiFunction<Long, Object, Number> asNumber = (longValue, object) -> (Number) longValue;
            //Execute
            Optional<Number> actual = Optional.of(target).map(toJoinedWith(asNumber));
            //Verify
            assertThat(actual).hasValue(3L);
        }
    }

    @Nested
    class toJoinedOptionalWith_2FunctionsTest {

        @Test
        void retrieve_optional_of_value() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            Function<Long, Number> asNumber = longValue -> (Number) longValue;
            Function<Object, Integer> asInteger = object -> Integer.parseInt(object.toString());
            //Execute
            Optional<Number> actual =
                    Optional.of(target)
                            .flatMap(
                                    toJoinedOptionalWith(
                                            asNumber,
                                            asInteger
                                    ));
            //Verify
            assertThat(actual).hasValue(3L);
        }
    }

    @Nested
    class toJoinedOptionalWith_biFunctionTest {
        @Test
        void retrieve_optional_of_value() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            BiFunction<Long, Object, Number> asNumber = (longValue, object) -> (Number) longValue;
            //Execute
            Optional<Number> actual = Optional.of(target).flatMap(toJoinedOptionalWith(asNumber));
            //Verify
            assertThat(actual).hasValue(3L);
        }
    }

    @Nested
    class acceptWith_2ConsumerTest {
        @Test
        void consume_and_return_self() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            Consumer<Object> objectNoOp = object -> {
            };
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            //Execute
            Optional.of(target).ifPresent(acceptWith(countUp, objectNoOp));

            //Verify
            assertThat(atomicLong).hasValue(3L);
        }
    }

    @Nested
    class acceptWith_biConsumerTest {
        @Test
        void consume_and_return_self() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            BiConsumer<Long, Object> countUp = (longValue, object) -> atomicLong.addAndGet(longValue);
            //Execute
            Optional.of(target).ifPresent(acceptWith(countUp));
            //Verify
            assertThat(atomicLong).hasValue(3L);
        }
    }

    @Nested
    class acceptLeftWithTest {
        @Test
        void consume_and_return_self() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            //Execute
            Optional.ofNullable(target).ifPresent(acceptLeftWith(countUp));
            //Verify
            assertThat(atomicLong).hasValue(3L);
        }
    }

    @Nested
    class acceptRightWithTest {
        @Test
        void consume_and_return_self() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            //Execute
            Optional.ofNullable(target).ifPresent(acceptRightWith(countUp));
            //Verify
            assertThat(atomicLong).hasValue(3L);
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
            Either<Long, String> actual = Optional.of(target)
                    .map(peekWith(
                            countUp,
                            objectNoOp))
                    .orElse(null);
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
            Either<Long, String> actual = Optional.of(target).map(peekWith(countUp)).orElse(null);
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
            Either<Long, String> actual = Optional.ofNullable(target).map(peekLeftWith(countUp)).orElse(null);
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
            Either<String, Long> actual = Optional.ofNullable(target).map(peekRightWith(countUp)).orElse(null);
            //Verify
            assertThat(atomicLong).hasValue(3L);
            assertThat(actual).isEqualTo(target);
        }
    }

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
    class Peek_2ConsumerTest {
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
                    .peek(
                            countUp,
                            objectNoOp);
            //Verify
            assertThat(atomicLong).hasValue(3L);
            assertThat(actual).isEqualTo(target);
        }
    }

    @Nested
    class Peek_biConsumerTest {
        @Test
        void consume_and_return_self() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            BiConsumer<Long, Object> countUp = (longValue, object) -> atomicLong.addAndGet(longValue);
            //Execute
            Either<Long, String> actual = target.peek(countUp);
            //Verify
            assertThat(atomicLong).hasValue(3L);
            assertThat(actual).isEqualTo(target);
        }
    }

    @Nested
    class PeekLeftTest {
        @Test
        void consume_and_return_self() {
            //SetUp
            Either<Long, String> target = EitherOf.left(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            //Execute
            Either<Long, String> actual = target.peekLeft(countUp);
            //Verify
            assertThat(atomicLong).hasValue(3L);
            assertThat(actual).isEqualTo(target);
        }
    }

    @Nested
    class PeekRightTest {
        @Test
        void consume_and_return_self() {
            //SetUp
            Either<String, Long> target = EitherOf.right(3L);
            AtomicLong atomicLong = new AtomicLong(0L);
            Consumer<Long> countUp = atomicLong::addAndGet;
            //Execute
            Either<String, Long> actual = target.peekRight(countUp);
            //Verify
            assertThat(atomicLong).hasValue(3L);
            assertThat(actual).isEqualTo(target);
        }
    }
}