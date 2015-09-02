(ns samutamm.routes.projects
  (:require [compojure.core :refer :all]
      [liberator.core :refer [defresource resource request-method-in]]
      [noir.io :as io]
      [clojure.java.io :refer [file]]
      [cheshire.core :refer [generate-string]]
      [samutamm.models.db :as database]
      [clojure.data.json :as json]
      [liberator.representation :refer [ring-response]]
      [clojure.string :as str]))


(defn parse-project [context] (json/read-str
                               (slurp (get-in context [:request :body]))
                               :key-fn keyword))

(defn add-tag-to-db [all-tags]
  (let [existing-tags (map (fn[t] (:text t)) (database/get-all-tags))
        tags (str/split all-tags #";")
        new-tags (filter (fn[tag] (nil? (some #{tag} existing-tags))) tags)]
    (loop [to-add new-tags]
      (if (empty? to-add)
        (count new-tags)
        (do
          (database/add-tag (first to-add))
          (recur (rest to-add)))))))

(defn add-project-to-database [project]
  (try
    (do
      (add-tag-to-db (:tags project))
      (database/update-or-create-project project))
    (catch Exception e (println (.getNextException e)))))


(defn project-is-valid [project]
  (let [fields [:projectname :description :tags :projectstart :projectend]]
    (every? true? (map (fn [field] (not (nil? (field project)))) fields))))

(defn auth-ok? [username password]
  (and
   (not (or (nil? username)(nil? password)))
   (do
     (println (database/get-user username))
     (= (:password (database/get-user username))
        password))))

(defresource get-all-projects
        :allowed-methods [:get]
        :available-media-types ["application/json"]
        :handle-ok (fn [_] (generate-string (database/get-all-projects))))

(defresource add-new-project
         :allowed-methods [:post]
         :available-media-types ["application/json"]
         :malformed? (fn[ctx] (let [project (parse-project ctx)
                                    updated-ctx (assoc ctx ::project project)
                                    result (conj [] (not (project-is-valid project)) updated-ctx)]
                                  result))
         :authorized? (fn [ctx] (let [project (::project ctx)]
                                  (auth-ok? (:username project)(:password))))
         :handle-malformed (fn [_] (generate-string (str "Malformed json!")))
         :post! (fn [ctx]  { ::data (generate-string (add-project-to-database  (::project ctx))) })
         :handle-created ::data)

(defresource delete-project [id]
         :allowed-methods [:delete]
         :available-media-types ["application/json"]
         :delete! (database/delete-project (Integer/parseInt id))
         :handle-no-content  (fn [_] (generate-string (str "deleted project"))))

(defresource get-project [id]
         :allowed-methods [:get]
         :available-media-types ["application/json"]
         :handle-ok  (fn [_] (generate-string (database/get-project (Integer/parseInt id)))))

(defresource image-credentials
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :handle-ok (fn [_] (generate-string {:bucket (System/getenv "BUCKET")
                                       :access_key (System/getenv "ACCESS_KEY")
                                       :secret_key (System/getenv "SECRET_KEY")})))

(defresource authenticate [cred]
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :authorized? (fn[_] (let [username (first (str/split cred #"-"))
                          password (second (str/split cred #"-"))]
                          (auth-ok? username password)))
  :handle-ok (fn[_] "OK"))

(defroutes project-routes
  (GET "/projects" request get-all-projects)
  (POST "/projects" request add-new-project)
  (DELETE "/projects/:id" [id] (delete-project id))
  (GET "/projects/:id" [id] (get-project id))
  (GET "/image-credentials" request image-credentials)
  (GET "/authenticate/:credentials" [credentials] (authenticate credentials)))
