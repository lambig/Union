[![Build Status](https://app.travis-ci.com/lambig/Either.svg?token=rcXQxJszfB4WCpwsWNgN&branch=main)](https://app.travis-ci.com/lambig/Either)
# Either<L, R>
Easy implementation of Union class-like something
## Instance Provider
EitherOf.class is the one and only instance provider.
### left(L): Left<L, R>
Returns instance with left value.  
```Either<Long, String> either = EitherOf.left(1L);```
### right(R): Right<L, R>
Returns instance with right value.  
```Either<Long, String> either = EitherOf.right("abc");```
### none(): Neither<L, R>
Returns instance without any value.  
```Either<Leng, String> neither = EitherOf.none();```
### l(L): LeftProvider<L>
Use this in case you need to declare type explicitly.  
```EitherOf.l(1L).<String>r();```
### r(R): RightProvider<R>
  Use this in case you need to declare type explicitly.  
```EitherOf.r("abc").<Long>l();```
  
## Supported methods
* left(): L
* right(): R
* leftOptional(): Optional<L>
* rightOptional(): Optional<R>
* hasLeft(): boolean
* hasRight(): boolean
* asJoined(Function<? super L, ? extends O>, Function<? super R, ? extends O>): O
* asJoined(BiFunction<? super L, ? super R, ? extends O>): O
* asJoinedOptional(Function<? super L, ? extends O>, Function<? super R, ? extends O>): Optional<O>
* asJoinedOptional(BiFunction<? super L, ? super R, ? extends O>): Optional<O>
* acceptWith(Consumer<? super L>, Consumer<? super R>): void
* acceptWith(BiConsumer<? super L, ? super R>): void
* acceptLeftWith(Consumer<? super L>): void
* acceptRightWith(Consumer<? super R>): void
* peekWith(Consumer<? super L>, Consumer<? super R>): Either<L, R>
* peekWith(BiConsumer<? super L, ? super R>): Either<L, R>
* peekLeftWith(Consumer<? super L>): Either<L, R>
* peekRightWith(Consumer<? super R>): Either<L, R>
