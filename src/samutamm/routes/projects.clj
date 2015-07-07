(ns samutamm.routes.projects
  (:require [compojure.core :refer :all]
      [liberator.core :refer [defresource resource request-method-in]]
      [noir.io :as io]
      [clojure.java.io :refer [file]]
      [cheshire.core :refer [generate-string]]
      [samutamm.models.db :as database]))


(def users (atom ["John" "Jane"]))

(defroutes project-routes
  (GET "/projects" []
       (resource
        :allowed-methods [:get]
        :handle-ok (fn [_] (generate-string (database/get-all-projects)))
        :available-media-types ["application/json"]))

  (POST "/projects" []
        (resource
         :allowed-methods [:post]
         :available-media-types ["application/json"]
         :post! (println "postia")
         :handle-created  (fn [_] (generate-string (str "created new project")))))

  (DELETE "/projects/:id" [id]
        (resource
         :allowed-methods [:delete]
         :available-media-types ["application/json"]
         :delete! (println (str "poistetaan id" id))
         :handle-no-content  (fn [_] (generate-string (str "deleted project")))))

  (PUT "/projects/:id" [id]
        (resource
         :allowed-methods [:put]
         :available-media-types ["application/json"]
         :åut! (println (str "muokataan " id))
         :handle-created  (fn [_] (generate-string (str "created new project"))))))

(defn check-for-empty
  [context]
  (let [params (get-in context [:request :form-params])]
    (empty? (get params "user"))))

(defresource add-user
  :method-allowed? (request-method-in :post)
  :malformed? (fn [context] (check-for-empty context))
  :handle-malformed "user name cannot be empty!"
  :post!
  (fn [context]
    (let [params (get-in context [:request :form-params])]
      (swap! users conj (get params "user"))))
  :handle-created (fn [_] (generate-string @users))
  :available-media-types ["application/json"])

