{:uberjar {:aot :all}
 :production
 {:ring {:open-browser? false,
         :stacktraces? false,
         :auto-reload? false}
  :env {:db-url "//localhost/projects"
        :db-user "postgres"
        :db-pass "postgres"}}
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
        :db-user "postgres"}}
 :repl {:dependencies [[org.clojure/tools.nrepl "0.2.10"]
                       [ring-mock "0.1.5"]
                       [ring/ring-devel "1.2.0"]
                       [midje "1.6.3"]]
        :env {:port 3000
              :db-url "//localhost/projects"
              :db-user "admin"
              :db-pass "admin"}}}
