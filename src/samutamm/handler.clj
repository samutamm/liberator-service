(ns samutamm.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.util.response :as response]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [samutamm.routes.home :refer [home-routes]]
            [samutamm.routes.projects :refer [project-routes]]
            [samutamm.models.db :as database]
            [environ.core :refer [env]]
            [liberator.dev :refer [wrap-trace]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defn init [] (database/migrate-db))

(defn destroy []
  (println "samutamm is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes home-routes project-routes app-routes)
      (handler/site)
      (wrap-base-url)))

(def header-buffer-size 1000000)

(defn -main[]
  (do
    (println "kohta käynnistyy!")
    (run-jetty app { :port 3000
                     :configurator (fn [jetty]
                                     (doseq [connector (.getConnectors jetty)]
                                       (.setRequestHeaderSize connector header-buffer-size)))})))
