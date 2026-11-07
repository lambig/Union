package example;

import io.github.lambig.union.Left;
import io.github.lambig.union.Right;
import io.github.lambig.union.Union;
import io.github.lambig.union.UnionOf;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UnionExample {
    public static void main(String[] args) {
        Left<Integer, String> left = UnionOf.l(42).<String>r();
        Right<Integer, String> right = UnionOf.r("invalid").<Integer>l();
        List<Union<Integer, String>> results = List.of(left, right);
        List<String> joined = results.stream()
                .map(Union.toJoinedWith(Object::toString, reason -> reason))
                .collect(Collectors.toList());
        List<String> events = new ArrayList<>();
        results.forEach(Union.acceptWith(n -> events.add("left:" + n), reason -> events.add("right:" + reason)));
        if (left.get() != 42 || !right.get().equals("invalid")
                || !joined.equals(List.of("42", "invalid"))
                || !events.equals(List.of("left:42", "right:invalid"))
                || !left.asJoined((n, reason) -> n == 42 && reason == null)
                || !right.asJoined((n, reason) -> n == null && reason.equals("invalid"))) {
            throw new AssertionError("Unexpected Union result");
        }
        System.out.println("left=" + left.get() + "; right=" + right.get()
                + "; joined=" + joined + "; events=" + events);
    }
}
