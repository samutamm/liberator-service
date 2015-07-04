(defproject samutamm "0.1.0-SNAPSHOT"
  :description "WEB app/page for samutamm"
  :url "http://samutamm.herokuapp.com"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [lib-noir "0.7.2"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [liberator "0.10.0"]
                 [cheshire "5.2.0"]
                 [ring/ring-jetty-adapter "1.2.2"]
                 [environ "0.5.0"]
                 [environ/environ.lein "0.2.1"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [postgresql/postgresql "9.1-901.jdbc4"]]
  :min-lein-version "2.0.0"
  :plugins [[lein-ring "0.8.12"]
            [lein-cloverage "1.0.6"]
            [lein-midje "3.1.3"]
            [lein-environ "0.5.0"]]
  :ring {:handler samutamm.handler/app
         :init samutamm.handler/init
         :destroy samutamm.handler/destroy}
  :uberjar-name "samutamm.jar"
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring {:open-browser? false,
           :stacktraces? false,
           :auto-reload? false}
    :env {:port 3000
          :db-url "//localhost/projects"
          :db-user "postgres"
          :db-pass ""}}
   :dev
   {:dependencies [[ring-mock "0.1.5"]
                   [ring/ring-devel "1.2.0"]
                   [midje "1.6.3"]]
    :env {:port 3000
          :db-url "//localhost/projects"
          :db-user "admin"
          :db-pass "admin"}}

   :travis-test
   {:dependencies [[ring-mock "0.1.5"]
                   [ring/ring-devel "1.2.0"]
                   [midje "1.6.3"]]
    :env {:port 3000
          :db-url "//localhost/testprojects"
          :db-user "postgres"
          :db-pass ""}}})
