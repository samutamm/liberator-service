(ns samutamm.test.handler
  (:use midje.sweet
        ring.mock.request
        samutamm.handler
        cheshire.core))

(def testproject {:id 77 :projectname "Big-T" :description "cool" :tags "#"
                            :projectstart {:year 2000 :month 6 :day 10} :projectend {:year 2000 :month 8 :day 10}})

(defn contains-string [body string]
    (let [body-string (slurp body)]
      (.contains body-string string)))

(defn execute-request [method-symbol path]
  (app (request method-symbol path)))

(defn create-request [project]
  (body
    (content-type
     (request :post "/projects")
     "application/json;charset=UTF-8")
    (generate-string project)))

(defn execute-request-with-json [project]
  (app (create-request project)))

;;tests
  (fact "image-credentials"
        (let [res (execute-request :get "/image-credentials")]
          (:status res) => 200))

(with-state-changes [(before :facts (init))]
  (fact "main route"
        (let [response (execute-request :get "/")]
          (:status response) => 200
          (:body response) => (fn [body] (contains-string body "<title>samutamm</title>"))))

  (fact "not-found route"
        (:status (execute-request :get "/invalidpath")) => 404)

  (fact "POST project"
        (let [project-count (count (parse-string (:body (execute-request :get "/projects"))))
              response (execute-request-with-json testproject)
              project-id (:id (parse-string (:body response) true))
              get-response (execute-request :get "/projects")]
          (count (parse-string (:body get-response))) => (inc project-count)
          (:status response) => 201
          (:body response) => (fn[body] (.contains body "id"))
          (:body get-response) => (fn[body] (.contains body (:projectname testproject))))
        (:status (execute-request-with-json {})) => 400
        (:status (execute-request-with-json (assoc testproject :tags nil))) => 400
        (:status (execute-request-with-json (dissoc testproject :description))) => 400)


  (with-state-changes [(before :facts (execute-request-with-json testproject))]
    (fact "GET all projects"
          (let [response (execute-request :get "/projects")
                fields ["id" "projectname" "description" "tags" "projectstart" "projectend" "created"]]
            (:status response) => 200
            (get (:headers response) "Content-Type") => "application/json;charset=UTF-8"
            (:body response) => (fn[body] (every? true?
                                                  (map  (fn [field]
                                                          (.contains body field)) fields))))))

  (fact "DELETE project/id returns status 204"
        (let [project-id  (:id (parse-string (:body (execute-request-with-json testproject)) true))
              project-count (count (parse-string (:body (execute-request :get "/projects"))))]
          (:status (execute-request :delete (str "/projects/" project-id))) => 204
          (count (parse-string (:body (execute-request :get "/projects")))) => (dec project-count)))

  (fact "GET single project"
        (let [project-id  (:id (parse-string (:body (execute-request-with-json testproject)) true))
              single-project (execute-request :get (str "/projects/" project-id))]
          (:body single-project) => (fn[b] (not (= b "Not Found")))
          (:body single-project) => (fn[body] (.contains body (:projectname testproject))))))
