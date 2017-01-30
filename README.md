# wine-perceptron

* [1/23] The perceptron is written and should train properly. However, the features are set up as column dominate while java is row dominate, needs a rewrite to fix

* [1/29] The perceptron is now row dominate but the IO needs to get rewritten to be more flexible with train/tune/test

* [1/30] The perceptron class has had some big changes now. The IO was modified to allow for all 3 files to be read in regardless of their order as well as began integrating the tuning set for early stopping. TODO: Finish the tuning for early stopping. 