## 1. Difference between @Configuration, @Component, and @Service annotations

- `@Component` is a generic stereotype for any Spring-managed component²³²⁶²⁷. Classes annotated with `@Component` are considered as candidates for auto-detection when using annotation-based configuration and classpath scanning²³²⁶.
- `@Configuration` is used to indicate that a class declares one or more `@Bean` methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime²³. `@Configuration` is meta-annotated with `@Component`, therefore `@Configuration` classes are candidates for component scanning²³. A `@Configuration` is also a `@Component`, but a `@Component` cannot act like a `@Configuration`²³.
- `@Service` is a specialization of `@Component` for more specific use cases, in the service layer²⁷. It holds the business logic and calls methods in the repository layer²⁷.

## 2. Customizing the component scanning process

Component scanning in Spring can be customized using the `@ComponentScan` annotation along with the `@Configuration` annotation to specify the packages that we want to be scanned¹⁴¹⁵. `@ComponentScan` without arguments tells Spring to scan the current package and all of its sub-packages¹⁴. There are also different types of filter options available with the `@ComponentScan` annotation to further customize the scanning process¹⁶.

## 3. Property value in two different active profiles

If a property is defined in two different active profiles, the value of the property from the last specified profile will be used¹². This is because Spring follows a "last-wins" strategy when several profiles are specified¹.

## 4. Using factory beans instead of regular beans

Factory beans are used when we need to control the creation of objects¹¹. This is particularly useful when the object creation process involves some logic that should be encapsulated within the factory¹¹. Regular beans are simply managed by the Spring container, and their lifecycle is controlled by the container¹¹. Factory beans give more control over object creation and can be beneficial in certain scenarios[^10^]¹¹.

## 5. Overriding any property defined in any .properties file

Properties in a `.properties` file can be overridden by specifying the property with a new value in another properties file that has higher precedence[^20^]²¹²². In the context of Spring, properties can also be overridden by passing them as JVM options¹⁸.

## 6. PostDestruct for a prototype scope bean

In Spring, `@PostConstruct` and `@PreDestroy` annotations are not honoured for prototype beans⁶⁷. This is because the Spring container does not manage the complete lifecycle of a prototype bean: the container instantiates, configures, and otherwise assembles a prototype object, and hands it to the client, with no further record of that prototype instance⁶⁷. Thus, although initialization lifecycle callback methods are called on all objects regardless of scope, in the case of prototypes, configured destruction lifecycle callbacks are not called⁶⁷.
