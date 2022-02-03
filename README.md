Assert Reflection Equals
----

The project is still in progress.

# Overall idea

We need to compare objects and their internal state via reflection. There are existing implementation but each one has drawbacks. The best one is from Unitils' `assertReflectionEquals()` - but it's buggy when it comes to very large graphs of objects and it becomes quite slow. But it should be used as a reference because it has many things implemented well.


Ideally in the end we'd like to create a Hamcrest matcher. Its current `samePropertyValuesAs()` has many downsides and doesn't seem to be usable in real projects:

* It uses properties instead of fields
* It can't work with ORM proxies
* It's a shallow comparison

Unitils' assertReflectionEquals() seems to solve this problem well in general (but it's not supported and it has its own issues), I wonder if Hamcrest can also solve this problem. Approx. syntax could be:

```
assertThat(actual,
    ReflectionMatcher.withDepth(1)
                     .matchEndNodesUsingEqualsThenField("id")
                     .unproxyIfNeeded(new HibernateUnproxer())
                     .excludingFields("blah")
                     .modes(COLLECTION_LINIENT_ORDER)
                     .equalsTo(expected)
);
```
where:
* `depth(0)` means infinite recursion
* `unproxyIfNeeded()` will return real object instead of Hibernate (or whatever) proxy. This can't work in Hamcrest Core as it will depend on ORM. So by default it will through an exception. Multiple one-class dependencies can be implemented for different ORMs. 
* `matchEndNodesUsingEqualsThenField("id")` is needed to compare fields at deepest level if depth != 0. The objects are compared with `equals()` by default, but those classes may not have equals() in which case we need to compare some representative field(s). This method may accept a map of `Class -> fieldNames[]` if different classes have different ID fields.
* `excludingFields()` should also be overloaded to accept `Class -> fieldNames[]`
* `modes()` is similar to Unitils' modes

Even if no one is going to implement it right now I wonder if such matcher would be accepted if I were to implement it some day?
