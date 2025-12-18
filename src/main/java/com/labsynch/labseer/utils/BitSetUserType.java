package com.labsynch.labseer.utils;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.BitSet;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

public class BitSetUserType implements UserType<BitSet> {

    public static final BitSetUserType INSTANCE = new BitSetUserType();

    @Override
    public int getSqlType() {
        return Types.OTHER;
    }

    @Override
    public Class<BitSet> returnedClass() {
        return BitSet.class;
    }

    @Override
    public boolean equals(BitSet x, BitSet y) {
        return (x == y) || (x != null && y != null && x.equals(y));
    }

    @Override
    public int hashCode(BitSet x) {
        return x != null ? x.hashCode() : 0;
    }

    @Override
    public BitSet nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        String stringBits = rs.getString(position);
        return (stringBits != null) ? SimpleUtil.stringToBitSet(stringBits) : null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, BitSet value, int index, SharedSessionContractImplementor session)
            throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            PGobject holder = new PGobject();
            holder.setType("BIT");
            holder.setValue(SimpleUtil.bitSetToString(value));
            st.setObject(index, holder);
        }
    }

    @Override
    public BitSet deepCopy(BitSet value) {
        return value == null ? null : (BitSet) value.clone();
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(BitSet value) {
        return deepCopy(value);
    }

    @Override
    public BitSet assemble(Serializable cached, Object owner) {
        return deepCopy((BitSet) cached);
    }
}