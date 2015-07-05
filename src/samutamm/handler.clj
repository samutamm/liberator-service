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
            [environ.core :refer [env]]))

(defn init []
  (do
    (println "inilializing")
    (println (str "database-url: " (env :db-url)))
    (database/migrate-db)
    (println (str "db-table is created: " (database/projects-table-is-created?)))))

(defn destroy []
  (println "samutamm is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes home-routes project-routes app-routes)
      (handler/site)
      (wrap-base-url)))
