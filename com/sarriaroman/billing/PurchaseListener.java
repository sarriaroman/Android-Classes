package com.sarriaroman.billing;

import com.sarriaroman.billing.BillingSecurity.VerifiedPurchase;

public interface PurchaseListener {
	public void purchaseCompleted( VerifiedPurchase purchase );
}
