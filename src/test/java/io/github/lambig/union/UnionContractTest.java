package io.github.lambig.union;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import static org.assertj.core.api.Assertions.*;

class UnionContractTest {
    @Test
    void only_the_present_side_supplies_the_result_and_effect() {
        for (Union<Integer, String> union : List.<Union<Integer, String>>of(UnionOf.left(42), UnionOf.right("invalid"))) {
            List<String> events = new ArrayList<>();
            assertThat(union.peek(n -> events.add("left"), s -> events.add("right"))).isSameAs(union);
            assertThat(events).containsExactly(union.hasLeft() ? "left" : "right");
            if (union.hasLeft()) {
                assertThat(union.asJoined(Object::toString, s -> { throw new AssertionError(); })).isEqualTo("42");
                union.acceptRight(s -> { throw new AssertionError(); });
            } else {
                assertThat(union.asJoined(n -> { throw new AssertionError(); }, Function.identity())).isEqualTo("invalid");
                union.acceptLeft(n -> { throw new AssertionError(); });
            }
        }
    }

    @Test
    void null_results_are_preserved_or_represented_as_empty() {
        for (Union<Integer, String> union : List.<Union<Integer, String>>of(UnionOf.left(42), UnionOf.right("invalid"))) {
            assertThat(union.<Object>asJoined(n -> null, s -> null)).isNull();
            assertThat(union.<Object>asJoined((n, s) -> null)).isNull();
            assertThat(union.asJoinedOptional(n -> null, s -> null)).isEmpty();
            assertThat(union.asJoinedOptional((n, s) -> null)).isEmpty();
        }
    }

    @Test
    void both_callbacks_are_required_even_for_the_absent_side() {
        Union<Integer, String> left = UnionOf.left(42);
        Union<Integer, String> right = UnionOf.right("invalid");
        assertThatNullPointerException().isThrownBy(() -> left.asJoined(Object::toString, null));
        assertThatNullPointerException().isThrownBy(() -> right.asJoined(null, Function.identity()));
        assertThatNullPointerException().isThrownBy(() -> left.acceptRight(null));
        assertThatNullPointerException().isThrownBy(() -> right.acceptLeft(null));
        assertThatNullPointerException().isThrownBy(() -> Union.acceptLeftWith(null));
        assertThatNullPointerException().isThrownBy(() -> Union.peekRightWith(null));
    }

    @Test
    void original_exception_and_prior_effects_reach_the_caller() {
        IllegalStateException failure = new IllegalStateException("failure");
        for (Union<Integer, String> union : List.<Union<Integer, String>>of(UnionOf.left(42), UnionOf.right("invalid"))) {
            assertThatThrownBy(() -> union.asJoinedOptional(n -> { throw failure; }, s -> { throw failure; })).isSameAs(failure);
            List<String> events = new ArrayList<>();
            Consumer<Object> recordAndFail = value -> { events.add(value.toString()); throw failure; };
            assertThatThrownBy(() -> union.peek(recordAndFail, recordAndFail)).isSameAs(failure);
            assertThat(events).containsExactly(union.hasLeft() ? "42" : "invalid");
        }
    }

    @Test
    void type_providers_validate_values_when_creating_the_union() {
        UnionOf.LeftProvider<Object> left = UnionOf.l(null);
        UnionOf.RightProvider<Object> right = UnionOf.r(null);
        assertThatNullPointerException().isThrownBy(left::r);
        assertThatNullPointerException().isThrownBy(right::l);
    }

    @Test
    void value_references_are_shared() {
        List<String> value = new ArrayList<>();
        Left<List<String>, Integer> union = UnionOf.left(value);
        value.add("changed");
        assertThat(union.left()).isSameAs(value).containsExactly("changed");
        assertThat(union.get()).isSameAs(value);
    }
}
