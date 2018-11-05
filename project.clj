(defproject game-cljs "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [reagent "0.7.0"]
                 [ring "1.6.3"]
                 [ring/ring-defaults "0.3.1"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [ring-server "0.5.0"]
                 [yogthos/config "1.1.1"]
                 [re-frame "0.10.5"]]

  :plugins [[lein-environ "1.1.0"]
            [lein-cljsbuild "1.1.7"]
            [lein-asset-minifier "0.2.7"
             :exclusions [org.clojure/clojure]]]

  :min-lein-version "2.5.3"
  :uberjar-name "game-cljs.jar"
  :main game-cljs.core
  :clean-targets ^{:protect false} 
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj" "src/cljs"]
  :resource-paths ["resources" "target/cljsbuild"]
  :minify-assets
  {:assets
   {"resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :figwheel {:css-dirs ["resources/public/css"]}

  :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}

  :cljsbuild
  {:builds
   {:min
    {:source-paths ["src/cljs"]
     :compiler {:output-to        "target/cljsbuild/public/js/app.js"
                :output-dir       "target/cljsbuild/public/js"
                :source-map       "target/cljsbuild/public/js/app.js.map"
                :optimizations :advanced
                :pretty-print  false}}
    }
   }
  :profiles {:uberjar {:hooks [minify-assets.plugin/hooks]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :aot :all
                       :omit-source true}})
