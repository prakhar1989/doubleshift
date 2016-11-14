(ns doubleshift.popup
  (:require [khroma.runtime :as runtime]
            [khroma.log :as console]
            [khroma.tabs :as tabs]
            [reagent.core :as r]
            [cljs.core.async :refer [>! <!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn greeting [msg]
  [:h4 msg])

(defn set-active
  "activates the tab in the current window"
  [tab]
  (let [{id :id} tab]
    (tabs/update id {:active true})))

(defn get-all-tabs []
  "gets all the open tabs in the current window"
  (let [tabs-chan (tabs/query {:currentWindow true})]
    (go (when-let [alltabs (<! tabs-chan)]
          (->> alltabs
               (map #(select-keys % [:url :title :id]))
               (rand-nth)
               (set-active))))))

(defn render-app []
  [:div
   [greeting "DoubleShift!"]
   [:input {:type "button" :value "Feeling lucky!"
            :on-click get-all-tabs}]])

(defn init []
  (r/render [render-app]
            (js/document.getElementById "app")))
