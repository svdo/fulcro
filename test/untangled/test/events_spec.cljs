(ns untangled.test.events-spec
  (:require-macros [cljs.test :refer (is deftest testing)]
                   [smooth-test.core :refer (specification behavior provided assertions)]
                   )
  (:require 
            smooth-test.stub
            [cljs.test :refer [do-report]]
            [untangled.test.events :as evt])
  )

(specification "Event Detector for detecting events"
               (let [detector (evt/event-detector)]
                 (behavior "can be used as a function" (is (fn? detector)))
                 (behavior "records that it saw an event"
                           (is (not (-> detector (evt/saw? :my-event))))
                           (detector :my-event)
                           (is (-> detector (evt/saw? :my-event)))
                           )
                 (behavior "records the number of times it has seen an event"
                           (is (= 0 (-> detector (evt/trigger-count :some-event))))
                           (detector :some-event)
                           (detector :some-event)
                           (detector :some-event)
                           (is (= 3 (-> detector (evt/trigger-count :some-event))))
                           )
                 (behavior "can be cleared"
                           (is (= 0 (-> detector (evt/trigger-count :other-event))))
                           (detector :other-event)
                           (is (= 1 (-> detector (evt/trigger-count :other-event))))
                           (evt/clear detector)
                           (is (= 0 (-> detector (evt/trigger-count :other-event))))
                           (is (= 0 (-> detector (evt/trigger-count :some-event))))
                           (is (= 0 (-> detector (evt/trigger-count :my-event))))
                           )
                 ))