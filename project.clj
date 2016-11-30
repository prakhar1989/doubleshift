(defproject doubleshift "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.107"]
                 [khroma "0.3.0"]
                 [reagent "0.6.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]]

  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-chromebuild "0.3.0"]]

  :cljsbuild {:builds {:main
                       {:compiler {:output-to "target/unpacked/doubleshift.js"
                                   :output-dir "target/js"}}}}
  :profiles {:dev {:cljsbuild
                   {:builds {:main
                             {:compiler {:optimizations :whitespace
                                         :pretty-print true}}}}}
             :prod {:cljsbuild
                    {:builds {:main
                              {:compiler {:optimizations :advanced
                                          :pretty-print false}}}}}})
