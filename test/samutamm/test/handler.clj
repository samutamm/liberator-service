(ns samutamm.test.handler
  (:use midje.sweet
        ring.mock.request
        samutamm.handler
        cheshire.core))

(def testproject {:id "77" :projectname "Big-T" :description "cool" :tags "#"
                            :projectstart {:year 2000 :month 6 :day 10} :projectend {:year 2000 :month 8 :day 10}})

(defn contains-string [body string]
    (let [body-string (slurp body)]
      (.contains body-string string)))

(defn create-request-and-execute [method-symbol path]
  (app (request method-symbol path)))

(defn create-request [project]
  (body
    (content-type
     (request :post "/projects")
     "application/json;charset=UTF-8")
    (generate-string project)))

(defn create-request-with-project-json [project]
  (app (create-request project)))

;;tests

(with-state-changes [(before :facts (init))]
  (fact "main route"
        (let [response (create-request-and-execute :get "/")]
          (:status response) => 200
          (:body response) => (fn [body] (contains-string body "<title>samutamm</title>"))))

  (fact "not-found route"
        (:status (create-request-and-execute :get "/invalidpath")) => 404)

  (fact "POST project"
        (let [response (create-request-with-project-json testproject)
              get-response (create-request-and-execute :get "/projects")]
          (:status response) => 201
          (:body response) => "{\"description\":\"cool\",\"tags\":\"#\",\"projectend\":{\"day\":10,\"month\":8,\"year\":2000},\"projectstart\":{\"day\":10,\"month\":6,\"year\":2000},\"id\":\"77\",\"projectname\":\"Big-T\"}"
          (:body get-response) => (fn[body] (.contains body (:id testproject))))
        (:status (create-request-with-project-json {})) => 400)

  ;; (:status (create-request-with-project-json (assoc testproject :tags nil))) => 400
  ;;      (:status (create-request-with-project-json (dissoc testproject :description))) => 400)


  (fact "GET project"
        (let [response (create-request-and-execute :get "/projects")
              fields ["id" "projectname" "description" "tags" "projectstart" "projectend" "created"]]
          (:status response) => 200
          (get (:headers response) "Content-Type") => "application/json;charset=UTF-8"
          (:body response) => (fn[body] (every? true?
                                                (map  (fn [field]
                                                        (.contains body field)) fields)))))

  (fact "DELETE project/id returns status 204"
        (:status (create-request-and-execute :delete "/projects/1")) => 204)

  (fact "PUT project/id return status 204"
        (:status (create-request-and-execute :put "/projects/1")) => 204))
