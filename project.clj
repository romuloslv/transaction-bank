(defproject transaction-bank "0.1.0-SNAPSHOT"
  :description "A simple application for managing transactions"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.191"]
                 [compojure "1.6.1"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-mock "0.3.2"]
                 [cheshire "5.8.1"]
                 [http-kit "2.3.0"]
                 [clj-time "0.14.0"]
                 [midje	"1.9.6"]]
  :plugins	[[lein-midje "3.2.1"]
             [lein-cloverage "1.0.13"]]
  :main ^:skip-aot transaction-bank.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
