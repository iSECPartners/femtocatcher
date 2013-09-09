femtocatcher
===========

FemtoCatcher runs on your Verizon Android smartphone and automatically switches your device into Airplane Mode, thus disabling all cellular connectivity, if it detects that your phone has connected to a femtocell (also called a Network Extender).  While this does render your cellular connectivity unavailable in areas where the strongest signal is a femtocell, we would rather have no service than be connected to a tower that could be used by an attacker to intercept our communications.  Network Extenders are dangerous and this tool can be used to help protect your privacy.  However, it is not perfect.

Here are some things to know about the app:
*	FemtoCatcher uses the network ID information available through Android API calls to determine if the phone is connected to a Femtocell.
*	We did not test how easy it would be for an attacker to change this information to fool the app, but certainly don’t rule out the possibility.
*	Some Verizon Android phones display an icon in the status bar and/or display an ERI banner of “Network Extender” when connected to a femtocell.  The strategy used by FemtoCatcher to detect the presence of a femtocell is based on the same techniques used by these indicators in Verizon ROMs.
*	FemtoCatcher will not automatically take your phone out of airplane mode when you move away from a femtocell.  You will be without service until you manually re-enable your connectivity.  If FemtoCatcher is running and you are in range of a femtocell when you disable airplane mode, FemtoCatcher will quickly put your phone back in airplane mode.
* Femtocatcher additionally provides a feature to disable radio when you are not connected to a 3G or a 4G network. The following networks are the available 3G and 4G networks:
  
  3G networks: EVDO_0, EVDO_A, EVDO_B and EHRPD

  4G networks: LTE

  If this feature is enabled, Femtocatcher detects whether the device is connected to the above networks. Otherwise, the device will be switched to Airplane mode. However, this feature could cycle into Airplane mode during the device start up. This is due to the fact that the device sometimes gradually improves the network connectivity by connecting to a 2G network first and then to 3G and 4G networks. As it detects a 2G network, the application switches to Airplane mode and upon disabling Airplane mode, as the device picks up a 2G network, the application switches the device back to Airplane mode. In this case, we strongly advise you to disable the feature to be able to connect to a network.
