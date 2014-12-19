# lein-sassc

[![Clojars Project](http://clojars.org/pazustep/lein-sassc/latest-version.svg)](http://clojars.org/pazustep/lein-sassc)

A Leiningen plugin to execute [SassC](https://github.com/sass/sassc). It simply
builds an equivalent command line for SassC and executes it. `lein-sassc` has
no dependencies other than Clojure 1.5+ and Java 1.7+.

## Installation

You can install the plugin by adding `pazustep/lein-sassc` to your `project.clj`
file in the `:plugins` section, like this:

```clojure
(defproject my-project "1.2.3"
  :plugins [[pazustep/lein-sassc "0.1.0"]])
```

## Configuration

All configuration goes under the `:sass` key. This could be a map, if you only
need to run `sassc` once, or a vector of maps if you need multiple runs.

Single run example, including all supported options:

```clojure
(defproject my-project "1.2.3"
  :plugins [[pazustep/lein-sassc "0.1.0"]]
  :sass {
    :src "src/scss/main.scss"
    :output-to "src/stylesheests/main.css"
    :executable "/usr/local/bin/sassc"
    :source-paths ["src/scss"]
    :compressed true
    :line-numbers true
    :source-map true
    :omit-map-comment true
    :precision 5})
```

`:src` and `:output-to` are required; all other keys are optional. Each key
maps directly to a `sassc` command line option, as follows:

- `:source-paths ["path1 "path2" ...]` adds `--load-path path1 --load-path path2 ...`
- `:compressed true` adds `--style compressed`
- `:line-numbers true` adds `--line-numbers`
- `:source-map true` adds `--sourcemap`
- `:omit-map-comment true` adds `--omit-map-comment`
- `:precision n` adds `--precision n`

The SassC executable can be set explicitly using the `:executable` key. If left out,
it will be run simply `sassc`.

## Usage

Once the plugin is installed and configured, you can run it with:

```sh
$ lein sassc
```

That's it. No options, no hooks, nothing.

## License

Copyright © 2014 Marcus Brito

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
