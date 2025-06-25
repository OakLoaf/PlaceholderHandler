# PlaceholderHandler
PkaceholderHandler is an annotation-based placeholder handler, the goal of the project is to make an abstract placeholder implementation that could be applied to many different projects with ease. 
Much of PlaceholderHandler's structure is based on the [Revxrsal/Lamp](https://github.com/Revxrsal/Lamp) command library.

## Goals
In the long-term, PlaceholderHandler aims to be platform independent meaning it will be able to support handling placeholders for any Java application, not just Bukkit based software.

## Dependency Information
![Version Number](https://repo.lushplugins.org/api/badge/latest/releases/org/lushplugins/PlaceholderHandler?color=40c14a&name=Latest)

<details open>
<summary>Maven</summary>

**Repository:**
```xml
<repositories>
    <repository>
        <id>lushplugins.org</id>
        <url>https://repo.lushplugins.org/releases/</url>
    </repository>
</repositories>
```
**Artifact:**
```xml
<dependencies>
    <dependency>
        <groupId>org.lushplugins</groupId>
        <artifactId>PlaceholderHandler</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```
</details>

<details>
<summary>Gradle</summary>

**Repository:**
```gradle
repositories {
    mavenCentral()
    maven { url = "https://repo.lushplugins.org/releases/" }
}
```
**Artifact:**
```gradle
dependencies {
    compileOnly "org.lushplugins.pluginupdater:PlaceholderHandler:1.0.0"
}
```
</details>

## Placeholder Class Example
```java
@Placeholder("example")
public class ExamplePlaceholders {

    @SubPlaceholder("uuid") // Will register as "%example_uuid%"
    public void uniqueId(@Nullable Player player) {
        return player != null ? player.getUniqueId().toString() : null;
    }

    @SubPlaceholder("name") // Will register as "%example_name%"
    public void name(@Nullable Player player) {
        return player != null ? player.getUniqueId().toString() : null;
    }

    @Placeholder("different_hello") // Will register as "%different_hello%"
    public String hello() {
        return "Hello World!";
    }
}
```

## Registering Placeholders
Registering your placeholders is very simple, simply create a PlaceholderHandler instance, and register your placeholders as an object.
```java
PlaceholderHandler placeholderHandler = PlaceholderHandler.builder(plugin).build();
placeholderHandler.register(new ExamplePlaceholders());
```

## Adding Parameter Providers
Creating a simple parameter provider for a class is very easy and can be done inline or by creating a class that implements `ParameterProvider`.
You can also use `ParameterProvider.Factory` which allows for much more specific implementations including parsing based on custom annotations.
```java
PlaceholderHandler.builder(this)
    .registerParameterProvider(UUID.class, (type, parameter, context) -> {
        Player player = context.player();
        return player != null ? player.getUniqueId() : null;
    })
    .build()
    .register(new ExamplePlaceholders());
```

## Getting support
If you need help setting up the plugin or have any questions, feel free to join the [LushPlugins discord server](https://discord.gg/mbPxvAxP3m)
