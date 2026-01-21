package com.labsynch.labseer.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.BitSet;

import io.hypersistence.utils.hibernate.type.ImmutableType;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.postgresql.util.PGobject;

public class BitSetUserType extends ImmutableType<BitSet> {

    public static final BitSetUserType INSTANCE = new BitSetUserType();

    public BitSetUserType() {
        super(BitSet.class);
    }

    @Override
    public int getSqlType() {
        return SqlTypes.OTHER;
    }

    @Override
    public BitSet get(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        String stringBits = rs.getString(position);
        return (stringBits != null) ? SimpleUtil.stringToBitSet(stringBits) : null;
    }

    @Override
    public void set(PreparedStatement st, BitSet value, int index, SharedSessionContractImplementor session)
            throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            PGobject holder = new PGobject();
            holder.setType("bit");
            holder.setValue(SimpleUtil.bitSetToString(value));
            st.setObject(index, holder);
        }
    }

    @Override
    public BitSet fromStringValue(CharSequence sequence) {
        return sequence != null ? SimpleUtil.stringToBitSet(sequence.toString()) : null;
    }
}