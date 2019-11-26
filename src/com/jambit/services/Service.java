package com.jambit.services;

import java.io.IOException;
import java.sql.SQLException;

public interface Service {

  Object run() throws IOException, SQLException;
}
