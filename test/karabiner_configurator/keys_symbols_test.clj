(ns karabiner-configurator.keys-symbols-test
  (:require [clojure.test                        :as t]
            [karabiner-configurator.rules        :as rules]
            [karabiner-configurator.keys-symbols :as sut]))

;; convert symbols
(def symbol-map {
  :⇧k   	:!SSk,
  :‹⇧k  	:!Sk,
  :⇧›k  	:!Rk,
  :⎇k   	:!OOk,
  :‹⌘k  	:!Ck,
  :⎇⇧b  	:!OO!SSb,
  :‹⎈##3	:!T##3
  :🌐    	:!F
})

;; convert modifiers
(def modi-map {
  :!OO!SSb     	:!OOSSb,
  :!CC!TT      	:!CCTT,
  :!CC!AA#BB!TT	:!CCAATT#BB,
  :!!##i       	:!!##i,
})

(t/deftest convert-symbols
  ;; convert symbols
  (t/testing "convert symbols"
    (doseq [[k v] symbol-map]
      (t/is (= (sut/key-name-sub-or-self k) v))
  ))

  ;; convert modifiers
  (t/testing "convert modifiers"
    (doseq [[k v] modi-map]
      (t/is (= (sut/move-modi-mandatory-front k) v))
  ))
  )
