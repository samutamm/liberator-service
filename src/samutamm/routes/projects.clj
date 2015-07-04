(ns samutamm.routes.projects
  (:require [compojure.core :refer :all]
      [liberator.core :refer [defresource resource request-method-in]]
      [noir.io :as io]
      [clojure.java.io :refer [file]]
      [cheshire.core :refer [generate-string]]
      [samutamm.models.db :as database]))


(def db-config (or (System/getenv "DATABASE_URL")
        {:subprotocol "postgresql"
         :subname "//localhost/projects"
         :user "admin"
         :password "admin"}))

;; alla esimerkkejä
(def users (atom ["John" "Jane"]))

(defresource get-projects
  :allowed-methods [:get]
  :handle-ok (fn [_] (generate-string (database/get-all-projects db-config)))
  :available-media-types ["application/json"])

(defresource delete-project [id]
  :allowed-methods [:delete]
  :handle-ok  "poisto"
  :available-media-types ["application/json"])

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

(defroutes project-routes
  (GET "/projects" request get-projects)
  (DELETE "/projects/:id" [id] (delete-project id)))
