(ns samutamm.routes.projects
  (:require [compojure.core :refer :all]
      [liberator.core :refer [defresource resource request-method-in]]
      [noir.io :as io]
      [clojure.java.io :refer [file]]
      [cheshire.core :refer [generate-string]]
      [samutamm.models.db :as database]
      [clojure.data.json :as json]))


(def users (atom ["John" "Jane"]))

(defn parse-project [context] (json/read-str
                               (slurp (get-in context [:request :body]))
                               :key-fn keyword))
(defn add-project-to-database
  [context] (let [project (parse-project context)
                                    id (+ 1 (count (database/get-all-projects)))]
                                (database/update-or-create-project id (:projectname project)
                                                                   (:description project)
                                                                   (:tags project)
                                                                   (:projectstart project)
                                                                   (:projectend project))))

(defn project-is-valid [project]
  (let [fields [:projectname :description :tags :projectstart :projectend]]
    (every? true? (map (fn [field] (not (nil? (field project)))) fields))))

(defresource get-all-projects
        :allowed-methods [:get]
        :handle-ok (fn [_] (generate-string (database/get-all-projects)))
        :available-media-types ["application/json"])

(defresource add-new-project
         :allowed-methods [:post]
         :available-media-types ["application/json"]
         :post! (fn [context] (println (:description
                                        (json/read-str
                                          (slurp (get-in context [:request :body]))
                                         :key-fn keyword))))
         :malformed? (fn[ctx] (not (project-is-valid (parse-project ctx))))
         :handle-malformed (fn [_] (generate-string (str "Malformed json!")))
         :allowed-methods [:post]
         :available-media-types ["application/json"]
         :post! (fn [ctx] "poh")
         :handle-created  (fn [_] (generate-string (str "created new project"))))

(defresource delete-project [id]
         :allowed-methods [:delete]
         :available-media-types ["application/json"]
         :delete! (println (str "poistetaan id" id))
         :handle-no-content  (fn [_] (generate-string (str "deleted project"))))

(defresource update-project [id]
         :allowed-methods [:put]
         :available-media-types ["application/json"]
         :put! (println (str "muokataan " id))
         :handle-ok  (fn [_] (generate-string (str "updated project"))))

(defroutes project-routes
  (GET "/projects" request get-all-projects)
  (POST "/projects" request add-new-project)
  (DELETE "/projects/:id" [id] (delete-project id))
  (PUT "/projects/:id" [id] (update-project id)))

