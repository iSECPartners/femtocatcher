femtocatcher
===========

FemtoCatcher runs on your Verizon Android smartphone and automatically switches your device into Airplane Mode, thus disabling all cellular connectivity, if it detects that your phone has connected to a femtocell (also called a Network Extender).  While this does render your cellular connectivity unavailable in areas where the strongest signal is a femtocell, we would rather have no service than be connected to a tower that could be used by an attacker to intercept our communications.  Network Extenders are dangerous and this tool can be used to help protect your privacy.  However, it is not perfect.

Here are some things to know about the app:
*	FemtoCatcher uses the network ID information available through Android API calls to determine if the phone is connected to a Femtocell.
*	We did not test how easy it would be for an attacker to change this information to fool the app, but certainly don’t rule out the possibility.
*	Some Verizon Android phones display an icon in the status bar and/or display an ERI banner of “Network Extender” when connected to a femtocell.  The strategy used by FemtoCatcher to detect the presence of a femtocell is based on the same techniques used by these indicators in Verizon ROMs.
*	FemtoCatcher will not automatically take your phone out of airplane mode when you move away from a femtocell.  You will be without service until you manually re-enable your connectivity.  If FemtoCatcher is running and you are in range of a femtocell when you disable airplane mode, FemtoCatcher will quickly put your phone back in airplane mode.
