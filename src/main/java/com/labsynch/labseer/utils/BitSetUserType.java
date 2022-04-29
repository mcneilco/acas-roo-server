package com.labsynch.labseer.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.BitSet;

import com.vladmihalcea.hibernate.type.ImmutableType;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.postgresql.util.PGobject;

public class BitSetUserType extends ImmutableType<BitSet> {

    public static final BitSetUserType INSTANCE = new BitSetUserType();

    public BitSetUserType() {
        super(BitSet.class);
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.OTHER};
    }

    @Override
    public BitSet get(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String stringBits = rs.getString(names[0]);
        return (stringBits != null) ? SimpleUtil.stringToBitSet(stringBits) : null;
    }

    @Override
    public void set(PreparedStatement st, BitSet value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            PGobject holder = new PGobject();
            holder.setType("BIT");
            holder.setValue(SimpleUtil.bitSetToString(value));
            st.setObject(index, holder);
        }
    }
}