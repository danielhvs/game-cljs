(ns game-cljs.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub :snake (fn [db _] (:snake db))) 
